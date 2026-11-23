package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.NavyGlowBrush
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.AiFetchState
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel
import kotlin.math.roundToInt

@Composable
fun AdminPanelScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var activeSection by remember { mutableStateOf("Overview") }
    val sections = listOf("Overview", "Controls", "Operations", "AI & Fraud")

    var commission by remember(state.adminConfig.commissionPercent) { mutableDoubleStateOf(state.adminConfig.commissionPercent) }
    var deliveryFee by remember(state.adminConfig.baseDeliveryFee) { mutableIntStateOf(state.adminConfig.baseDeliveryFee) }
    var surge by remember(state.adminConfig.surgeMultiplier) { mutableDoubleStateOf(state.adminConfig.surgeMultiplier) }
    var dynamicPricing by remember(state.adminConfig.dynamicPricingEnabled) { mutableStateOf(state.adminConfig.dynamicPricingEnabled) }
    var newCouponCode by remember { mutableStateOf("") }
    var newCouponFlat by remember { mutableStateOf("75") }

    val totalRevenue = state.orders.sumOf { it.amount } + 1842600
    val platformCommission = (totalRevenue * state.adminConfig.commissionPercent / 100.0).roundToInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_panel_screen")
    ) {
        GlassSubScreenHeader(
            title = "Super Admin Console",
            subtitle = "Unified Platform Analytics, Pricing, KYC & AI Fraud Shield",
            onBack = { viewModel.navigateBack() },
            trailing = { StatusPill("ADMIN", PremiumPink) }
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sections) { sec ->
                GlassChip(sec, activeSection == sec, { activeSection = sec })
            }
        }

        Spacer(Modifier.height(10.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth >= 600.dp
            val kpiColumns = if (isWide) 4 else 2

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (activeSection) {
                    "Overview" -> {
                        item {
                            Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 24.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("Platform Gross Revenue (Demo)", style = MaterialTheme.typography.labelSmall, color = LightSkyBlue)
                                    Text("₹$totalRevenue", style = MaterialTheme.typography.displayMedium, color = PureWhite, fontWeight = FontWeight.ExtraBold)
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Commission: ₹$platformCommission", style = MaterialTheme.typography.bodySmall, color = LightPink)
                                        Text("Pending Settlements: ₹1,12,400", style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                                    }
                                }
                            }
                        }

                        item {
                            val metrics = listOf(
                                "Total Users" to "48,920",
                                "Active Drivers" to "3,412",
                                "Restaurants" to "${state.restaurants.size * 64}",
                                "Grocery Shops" to "218",
                                "Active Rides" to "${state.orders.count { it.serviceType.name == "RIDE" } + 142}",
                                "Food Orders" to "${state.orders.count { it.serviceType.name == "FOOD" } + 628}",
                                "Grocery Orders" to "${state.orders.count { it.serviceType.name == "GROCERY" } + 419}",
                                "Parcel + Truck" to "${state.orders.count { it.serviceType.name == "PARCEL" || it.serviceType.name == "TRANSPORT" } + 187}"
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                metrics.chunked(kpiColumns).forEach { row ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                        row.forEach { (label, value) ->
                                            Glass3DCard(modifier = Modifier.weight(1f), cornerRadius = 18.dp, contentPadding = PaddingValues(14.dp)) {
                                                Column {
                                                    Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                    Spacer(Modifier.height(4.dp))
                                                    Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "Controls" -> {
                        item {
                            Glass3DCard(cornerRadius = 22.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("Commission, Delivery & Dynamic Surge", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Platform Commission: ${"%.1f".format(commission)}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    Slider(
                                        value = commission.toFloat(),
                                        onValueChange = { commission = it.toDouble() },
                                        valueRange = 5f..30f
                                    )
                                    Text("Base Delivery Charge: ₹$deliveryFee", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                    Slider(
                                        value = deliveryFee.toFloat(),
                                        onValueChange = { deliveryFee = it.roundToInt() },
                                        valueRange = 10f..99f
                                    )
                                    Text("Live Surge Multiplier: ${"%.1f".format(surge)}x (Updates Ride & Truck fares)", style = MaterialTheme.typography.bodySmall, color = PremiumPink)
                                    Slider(
                                        value = surge.toFloat(),
                                        onValueChange = { surge = ((it * 10).roundToInt() / 10.0) },
                                        valueRange = 1.0f..2.5f
                                    )
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text("Dynamic Pricing Engine", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                        Switch(checked = dynamicPricing, onCheckedChange = { dynamicPricing = it })
                                    }
                                    GradientPillButton(
                                        text = "Apply Live Pricing Config",
                                        onClick = {
                                            viewModel.updateAdminPricing(commission, deliveryFee, surge, dynamicPricing)
                                            viewModel.showMessage("Live pricing & surge (${"%.1f".format(surge)}x) applied across Super App!")
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        testTagName = "admin_apply_pricing"
                                    )
                                }
                            }
                        }

                        item {
                            Glass3DCard(cornerRadius = 22.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("City & Service Area Management", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                    state.adminConfig.activeCities.forEach { (city, enabled) ->
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            Text(city, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                            Switch(checked = enabled, onCheckedChange = { viewModel.toggleAdminCity(city) })
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Glass3DCard(cornerRadius = 22.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Publish Platform Coupon", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = newCouponCode,
                                            onValueChange = { newCouponCode = it },
                                            label = { Text("Code (e.g. SKY100)") },
                                            modifier = Modifier.weight(1.2f)
                                        )
                                        OutlinedTextField(
                                            value = newCouponFlat,
                                            onValueChange = { newCouponFlat = it.filter(Char::isDigit) },
                                            label = { Text("Flat ₹ Off") },
                                            modifier = Modifier.weight(0.8f)
                                        )
                                    }
                                    GradientPillButton("Publish Coupon", {
                                        viewModel.addAdminCoupon(newCouponCode, newCouponFlat.toIntOrNull() ?: 50, 199)
                                        newCouponCode = ""
                                    }, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }

                    "Operations" -> {
                        item {
                            Glass3DCard(cornerRadius = 22.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Partner Verification Queue", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(state.partnerProfile.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                                            Text("${state.partnerProfile.category.label} • RC: ${state.partnerProfile.vehicleRc}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        if (state.partnerProfile.verificationStatus == "Verified") {
                                            StatusPill("Verified", EmeraldSuccess)
                                        } else {
                                            GradientPillButton("Approve KYC", { viewModel.approvePartnerKyc() })
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Support Complaints & Refunds", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }

                        items(state.complaints, key = { it.id }) { cmp ->
                            Glass3DCard(cornerRadius = 18.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("${cmp.id} • ${cmp.userName}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                                        StatusPill(cmp.status, if (cmp.status == "Resolved") EmeraldSuccess else AmberWarning)
                                    }
                                    Text(cmp.topic, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (cmp.status != "Resolved") {
                                        OutlinedButton(onClick = { viewModel.resolveComplaint(cmp.id) }) {
                                            Text("Resolve & Credit ₹${cmp.refundAmount}")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "AI & Fraud" -> {
                        item {
                            Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 24.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.AutoAwesome, null, tint = SkyBlue)
                                        Text("Gemini AI Demand, Route & Fraud Engine", style = MaterialTheme.typography.titleMedium, color = PureWhite)
                                    }
                                    GradientPillButton(
                                        text = "Run Live Gemini Ops Analysis",
                                        icon = Icons.Default.AutoAwesome,
                                        onClick = { viewModel.loadAiOperationalInsights() },
                                        modifier = Modifier.fillMaxWidth(),
                                        testTagName = "admin_run_ai_ops"
                                    )
                                    when (val res = state.aiOpsInsightState) {
                                        AiFetchState.Idle -> Text("Tap above to generate live Gemini demand predictions, driver matching strategy, and fraud analysis.", style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                                        AiFetchState.Loading -> CircularProgressIndicator(color = SkyBlue)
                                        is AiFetchState.Error -> Text("AI Error: ${res.message}", style = MaterialTheme.typography.bodySmall, color = LightPink)
                                        is AiFetchState.Success -> {
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text("Demand Forecast: ${res.data.demandForecast}", style = MaterialTheme.typography.bodySmall, color = PureWhite)
                                                Text("Route & Driver Tip: ${res.data.routeOptimizationTip}", style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                                                Text("Fraud Shield: ${res.data.suspiciousAlertSummary}", style = MaterialTheme.typography.bodySmall, color = LightPink)
                                                StatusPill("Suggested Surge: ${res.data.suggestedSurgeMultiplier}x", EmeraldSuccess)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Suspicious Booking Alerts", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }

                        items(state.fraudAlerts, key = { it.id }) { alert ->
                            Glass3DCard(cornerRadius = 18.dp) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Shield, null, tint = if (alert.resolved) EmeraldSuccess else RoseError)
                                            Text("${alert.id} • ${alert.bookingId}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                        StatusPill(if (alert.resolved) "Resolved" else "${alert.riskLevel} Risk", if (alert.resolved) EmeraldSuccess else RoseError)
                                    }
                                    Text(alert.reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (!alert.resolved) {
                                        OutlinedButton(onClick = { viewModel.resolveFraudAlert(alert.id) }) {
                                            Text("Mark Reviewed & Clear")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
