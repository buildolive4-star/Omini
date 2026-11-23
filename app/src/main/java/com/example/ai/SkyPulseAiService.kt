package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "SkyPulseAiService"
private const val DEFAULT_MODEL = "gemini-3.8-flash"

data class AiSmartRecommendation(
    val title: String,
    val category: String,
    val reason: String,
    val estimatedEta: String,
    val suggestedAction: String
)

data class AiOperationalInsight(
    val demandForecast: String,
    val routeOptimizationTip: String,
    val suspiciousAlertSummary: String,
    val suggestedSurgeMultiplier: Double
)

class SkyPulseAiService {

    private val chatModel by lazy {
        Firebase.ai.generativeModel(
            modelName = DEFAULT_MODEL,
            systemInstruction = content {
                text(
                    "You are SkyPulse AI, the built-in smart concierge for the SkyPulse All-in-One " +
                        "Hyperlocal Super App (combining Ride Booking, Food Delivery, Grocery Shopping, " +
                        "Parcel Delivery, Goods Transport, Partner Dashboards, and Admin Operations). " +
                        "Be concise, friendly, practical, and structure answers clearly with bullet points. " +
                        "Remember this is a prototype demo app; remind users when needed that bookings " +
                        "and tracking are simulated."
                )
            }
        )
    }

    private val recommendationModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "recommendations" to Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "title" to Schema.string("Short catchy title of the recommended item or service"),
                            "category" to Schema.enumeration(
                                listOf("Ride", "Food", "Grocery", "Parcel", "Transport"),
                                description = "Super app service category"
                            ),
                            "reason" to Schema.string("One-sentence personalized reason"),
                            "estimatedEta" to Schema.string("Short ETA like 12 mins or 25 mins"),
                            "suggestedAction" to Schema.string("Short button label like Order Now or Book Bike")
                        )
                    )
                )
            )
        )
        Firebase.ai.generativeModel(
            modelName = DEFAULT_MODEL,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    private val opsInsightModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "demandForecast" to Schema.string("2-sentence hyperlocal demand prediction across rides, food, grocery, parcel"),
                "routeOptimizationTip" to Schema.string("2-sentence delivery route optimization and smart driver matching recommendation"),
                "suspiciousAlertSummary" to Schema.string("Concise fraud and suspicious booking risk assessment"),
                "suggestedSurgeMultiplier" to Schema.double("Recommended surge multiplier between 1.0 and 2.2")
            )
        )
        Firebase.ai.generativeModel(
            modelName = DEFAULT_MODEL,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    fun streamSupportReply(
        userQuery: String,
        appContextSummary: String
    ): Flow<String> = flow {
        val prompt = buildString {
            appendLine("Current App Context: $appContextSummary")
            appendLine("Customer / Operator Question: $userQuery")
            appendLine("Give a helpful, concise response tailored to SkyPulse.")
        }
        val accumulated = StringBuilder()
        try {
            withTimeout(60_000L) {
                chatModel.generateContentStream(prompt).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(accumulated.toString())
                    }
                }
            }
            if (accumulated.isBlank()) {
                throw IllegalStateException("Gemini returned an empty response.")
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Streaming chat timed out", e)
            throw IllegalStateException("AI assistant timed out after 60 seconds. Please retry.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Streaming chat failed", e)
            throw e
        }
    }

    suspend fun fetchSmartRecommendations(
        city: String,
        recentHistory: String
    ): List<AiSmartRecommendation> {
        return try {
            val response = withTimeout(60_000L) {
                recommendationModel.generateContent(
                    "Generate 4 hyperlocal smart recommendations for a SkyPulse user in $city. " +
                        "Recent activity: $recentHistory. Mix Ride, Food, Grocery, and Parcel ideas."
                )
            }
            val raw = response.text ?: throw IllegalStateException("Empty recommendation payload.")
            val root = JSONObject(raw)
            val arr = root.getJSONArray("recommendations")
            (0 until arr.length()).map { idx ->
                val obj = arr.getJSONObject(idx)
                AiSmartRecommendation(
                    title = obj.optString("title", "Express Pick"),
                    category = obj.optString("category", "Food"),
                    reason = obj.optString("reason", "Popular near your location right now."),
                    estimatedEta = obj.optString("estimatedEta", "18 mins"),
                    suggestedAction = obj.optString("suggestedAction", "Explore")
                )
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Smart recommendations timed out", e)
            throw IllegalStateException("Smart recommendations timed out. Please try again.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Smart recommendations failed", e)
            throw e
        }
    }

    suspend fun fetchOperationalInsights(
        city: String,
        activeOrderCount: Int,
        activeDriverCount: Int,
        flaggedCount: Int
    ): AiOperationalInsight {
        return try {
            val response = withTimeout(60_000L) {
                opsInsightModel.generateContent(
                    "Analyze live SkyPulse operations in $city: $activeOrderCount active orders, " +
                        "$activeDriverCount active partners online, $flaggedCount flagged suspicious bookings. " +
                        "Provide demand prediction, delivery route optimization & driver matching advice, " +
                        "fraud alert summary, and recommended surge multiplier."
                )
            }
            val raw = response.text ?: throw IllegalStateException("Empty ops insight payload.")
            val json = JSONObject(raw)
            AiOperationalInsight(
                demandForecast = json.optString("demandForecast"),
                routeOptimizationTip = json.optString("routeOptimizationTip"),
                suspiciousAlertSummary = json.optString("suspiciousAlertSummary"),
                suggestedSurgeMultiplier = json.optDouble("suggestedSurgeMultiplier", 1.2)
                    .coerceIn(1.0, 2.2)
            )
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Ops insights timed out", e)
            throw IllegalStateException("AI ops analysis timed out. Please retry.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Ops insights failed", e)
            throw e
        }
    }
}
