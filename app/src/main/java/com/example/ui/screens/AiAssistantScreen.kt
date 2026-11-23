package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppRoute
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.ShimmerSkeletonCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.NavyGlowBrush
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.AiFetchState
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

@Composable
fun AiAssistantScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel,
    onVoicePromptClick: () -> Unit
) {
    BackHandler { viewModel.navigateBack() }

    var promptInput by remember { mutableStateOf("") }
    val quickPrompts = listOf(
        "Best dinner + grocery combo under ₹450?",
        "Fastest ride option to airport right now?",
        "Optimize my parcel route & ETA",
        "Check my wallet & best coupon code"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .testTag("ai_assistant_screen")
    ) {
        GlassSubScreenHeader(
            title = "SkyPulse Gemini AI",
            subtitle = "Live AI Concierge • Smart Picks • Route & ETA Intelligence",
            onBack = { viewModel.navigateBack() },
            trailing = { StatusPill("GEMINI 3.8", SkyBlue) }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Structured AI Smart Recommendations Card
            item {
                Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 24.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, null, tint = SkyBlue, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Smart Hyperlocal Picks", style = MaterialTheme.typography.titleMedium, color = PureWhite)
                            }
                            StatusPill(state.profile.currentCity, PremiumPink)
                        }

                        when (val recState = state.aiRecommendationsState) {
                            AiFetchState.Idle -> {
                                Text(
                                    "Generate personalized Ride, Food, Grocery & Parcel recommendations powered by Firebase AI Logic.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightSkyBlue
                                )
                                GradientPillButton(
                                    text = "Generate AI Recommendations",
                                    icon = Icons.Default.AutoAwesome,
                                    onClick = { viewModel.loadAiSmartRecommendations() },
                                    modifier = Modifier.fillMaxWidth(),
                                    testTagName = "generate_ai_recs_button"
                                )
                            }
                            AiFetchState.Loading -> {
                                ShimmerSkeletonCard(height = 72.dp)
                            }
                            is AiFetchState.Error -> {
                                Text(recState.message, style = MaterialTheme.typography.bodySmall, color = LightPink)
                                GradientPillButton(
                                    text = "Retry AI Recommendations",
                                    onClick = { viewModel.loadAiSmartRecommendations() },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            is AiFetchState.Success -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    recState.data.forEach { rec ->
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = PureWhite.copy(alpha = 0.12f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text("${rec.title} (${rec.category})", style = MaterialTheme.typography.titleSmall, color = PureWhite)
                                                    Text(rec.reason, style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                                                    Text("Est. ETA: ${rec.estimatedEta}", style = MaterialTheme.typography.labelSmall, color = LightPink)
                                                }
                                                Spacer(Modifier.width(8.dp))
                                                GlassChip(
                                                    text = rec.suggestedAction,
                                                    selected = true,
                                                    onClick = {
                                                        val route = when (rec.category) {
                                                            "Ride" -> AppRoute.Ride
                                                            "Food" -> AppRoute.Food
                                                            "Grocery" -> AppRoute.Grocery
                                                            "Parcel" -> AppRoute.Parcel
                                                            else -> AppRoute.Transport
                                                        }
                                                        viewModel.navigateTo(route)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Prompt Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(quickPrompts) { qp ->
                        GlassChip(
                            text = qp,
                            selected = false,
                            onClick = { viewModel.sendAiChatQuery(qp) }
                        )
                    }
                }
            }

            // Streaming Chat Messages
            items(state.aiChatMessages, key = { it.id }) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (msg.isUser) 20.dp else 6.dp,
                            bottomEnd = if (msg.isUser) 6.dp else 20.dp
                        ),
                        color = if (msg.isUser) DarkNavy else GlassWhiteHigh,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth(0.88f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (msg.isUser) "You" else "SkyPulse AI",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (msg.isUser) LightSkyBlue else PremiumPink,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = msg.text.ifBlank { "Thinking…" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (msg.isUser) PureWhite else DarkNavy
                            )
                        }
                    }
                }
            }

            state.aiChatError?.let { err ->
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PalePink,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Gemini Notice: $err",
                            style = MaterialTheme.typography.bodySmall,
                            color = RoseError,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Chat Input Bar + Voice Mic
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Ask SkyPulse AI anything…") },
                leadingIcon = {
                    IconButton(onClick = onVoicePromptClick) {
                        Icon(Icons.Default.Mic, "Voice Prompt", tint = PremiumPink)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    focusedBorderColor = SkyBlue,
                    unfocusedBorderColor = PaleSky
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input")
            )

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = DarkNavy,
                modifier = Modifier.size(52.dp)
            ) {
                IconButton(
                    enabled = !state.aiChatLoading && promptInput.isNotBlank(),
                    onClick = {
                        viewModel.sendAiChatQuery(promptInput)
                        promptInput = ""
                    },
                    modifier = Modifier.testTag("ai_chat_send_button")
                ) {
                    if (state.aiChatLoading) {
                        CircularProgressIndicator(color = SkyBlue, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send to Gemini", tint = SkyBlue)
                    }
                }
            }
        }
    }
}
