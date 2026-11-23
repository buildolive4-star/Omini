package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppRoute
import com.example.data.OrderRecord
import com.example.data.ServiceType
import com.example.ui.components.AnimatedOrderStepper
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel
import kotlin.math.roundToInt

@Composable
fun MultiServiceCartScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var couponInput by remember { mutableStateOf(state.appliedCouponCode) }
    val subtotal = state.cartItems.sumOf { it.unitPrice * it.quantity }
    val deliveryFee = if (state.cartItems.isEmpty()) 0 else state.adminConfig.baseDeliveryFee
    val taxes = (subtotal * 0.05).roundToInt()
    val appliedCoupon = state.coupons.find { it.code == state.appliedCouponCode }
    val discount = if (appliedCoupon != null && subtotal >= appliedCoupon.minOrder) appliedCoupon.discountFlat else 0
    val grandTotal = (subtotal + deliveryFee + taxes - discount).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("multi_cart_screen")
    ) {
        GlassSubScreenHeader(
            title = "Multi-Service Cart",
            subtitle = "Food + Grocery combined in one seamless checkout",
            onBack = { viewModel.navigateBack() }
        )

        if (state.cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Glass3DCard(cornerRadius = 26.dp) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ShoppingCart, null, tint = SkyBlue, modifier = Modifier.size(52.dp))
                        Text("Your Multi-Service Cart is Empty", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            "Mix restaurant dishes and daily grocery essentials into a single delivery checkout.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GradientPillButton("Browse Food", { viewModel.navigateTo(AppRoute.Food) }, modifier = Modifier.weight(1f))
                            GradientPillButton("Shop Grocery", { viewModel.navigateTo(AppRoute.Grocery) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.cartItems, key = { "${it.itemId}_${it.customization}" }) { item ->
                    Glass3DCard(cornerRadius = 20.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                StatusPill(item.serviceType.label, if (item.serviceType == ServiceType.FOOD) PremiumPink else SkyBlue)
                                Text(item.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    "${item.sourceName}${if (item.customization.isNotBlank()) " • ${item.customization}" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text("₹${item.unitPrice * item.quantity}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkNavy)
                            ) {
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(item.itemId, item.customization, -1) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(Icons.Default.Remove, "Decrease", tint = PureWhite, modifier = Modifier.size(16.dp))
                                }
                                Text("${item.quantity}", style = MaterialTheme.typography.titleSmall, color = PureWhite)
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(item.itemId, item.customization, 1) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(Icons.Default.Add, "Increase", tint = SkyBlue, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // Coupon Code Card
                item {
                    Glass3DCard(cornerRadius = 22.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ConfirmationNumber, null, tint = PremiumPink)
                                Spacer(Modifier.width(8.dp))
                                Text("Apply Promo Coupon", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = couponInput,
                                    onValueChange = { couponInput = it },
                                    placeholder = { Text("Enter SKYPULSE50") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("coupon_code_input")
                                )
                                GradientPillButton(
                                    text = "Apply",
                                    onClick = { viewModel.applyCoupon(couponInput) },
                                    testTagName = "apply_coupon_button"
                                )
                            }
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(state.coupons) { coupon ->
                                    GlassChip(
                                        text = "${coupon.code} (-₹${coupon.discountFlat})",
                                        selected = state.appliedCouponCode == coupon.code,
                                        onClick = {
                                            couponInput = coupon.code
                                            viewModel.applyCoupon(coupon.code)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Delivery Address & Payment Mode
                item {
                    Glass3DCard(cornerRadius = 22.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Delivery Address", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text(state.profile.currentLocation, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            HorizontalDivider(color = PaleSky)
                            Text("Payment Method", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(listOf("SkyPulse Wallet", "UPI", "Card •••• 4092", "Net Banking", "Cash on Delivery")) { mode ->
                                    GlassChip(
                                        text = mode,
                                        selected = state.selectedPaymentMethod == mode,
                                        onClick = { viewModel.selectPaymentMethod(mode) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Price Breakdown
                item {
                    Glass3DCard(cornerRadius = 22.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Bill Breakdown", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            BillRow("Items Subtotal", "₹$subtotal")
                            BillRow("Unified Delivery Fee", "₹$deliveryFee")
                            BillRow("GST & Platform Taxes (5%)", "₹$taxes")
                            if (discount > 0) {
                                BillRow("Coupon Discount (${state.appliedCouponCode})", "-₹$discount", highlight = true)
                            }
                            HorizontalDivider(color = LightSkyBlue)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Grand Total", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                                Text("₹$grandTotal", style = MaterialTheme.typography.headlineSmall, color = PremiumPink, fontWeight = FontWeight.ExtraBold)
                            }
                            Spacer(Modifier.height(6.dp))
                            GradientPillButton(
                                text = "PLACE ORDER • ₹$grandTotal",
                                onClick = { viewModel.placeMultiServiceCartOrder() },
                                modifier = Modifier.fillMaxWidth(),
                                testTagName = "place_cart_order_button"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BillRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = if (highlight) EmeraldSuccess else MutedNavy)
        Text(value, style = MaterialTheme.typography.titleSmall, color = if (highlight) EmeraldSuccess else DarkNavy)
    }
}

@Composable
fun OrdersHubScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel,
    initialFilter: ServiceType? = null,
    showBackButton: Boolean = false
) {
    if (showBackButton) {
        BackHandler { viewModel.navigateBack() }
    }

    var selectedFilter by remember(initialFilter) { mutableStateOf(initialFilter) }
    val filtered = state.orders.filter { selectedFilter == null || it.serviceType == selectedFilter }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("orders_hub_screen")
    ) {
        if (showBackButton) {
            GlassSubScreenHeader(
                title = selectedFilter?.let { "My ${it.label} History" } ?: "My Orders & Tracking",
                subtitle = "Live order progress, OTPs & One-Tap Rebooking",
                onBack = { viewModel.navigateBack() }
            )
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Orders & Live Tracking", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                Text("Advance live demo tracking or rebook any order in 1 tap", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GlassChip("All (${state.orders.size})", selectedFilter == null, { selectedFilter = null })
            }
            items(ServiceType.entries) { type ->
                GlassChip(type.label, selectedFilter == type, { selectedFilter = type })
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 112.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered, key = { it.id }) { order ->
                OrderTrackingCard(
                    order = order,
                    onAdvance = { viewModel.advanceOrderStatus(order.id) },
                    onRebook = { viewModel.oneTapRebook(order) },
                    onRefund = { viewModel.requestGroceryRefund(order.id) }
                )
            }
        }
    }
}

@Composable
private fun OrderTrackingCard(
    order: OrderRecord,
    onAdvance: () -> Unit,
    onRebook: () -> Unit,
    onRefund: () -> Unit
) {
    val steps = when (order.serviceType) {
        ServiceType.FOOD -> listOf("Placed", "Accepted", "Cooking", "Pickup", "On Way", "Delivered")
        ServiceType.GROCERY -> listOf("Placed", "Packed", "On Way", "Delivered")
        ServiceType.PARCEL -> listOf("Booked", "Matched", "Pickup", "Transit", "POD")
        ServiceType.TRANSPORT -> listOf("Booked", "Loading", "Transit", "Done")
        ServiceType.RIDE -> listOf("Matched", "Arriving", "Trip", "Done")
    }

    Glass3DCard(
        cornerRadius = 22.dp,
        modifier = Modifier.testTag("order_card_${order.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(order.serviceType.label, SkyBlue)
                    StatusPill(order.status, if (order.isLive) PremiumPink else EmeraldSuccess)
                }
                Text("₹${order.amount}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
            }

            Column {
                Text(order.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(order.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${order.id} • ${order.timestamp} • ${order.paymentMethod}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (order.pickupOtp.isNotBlank() || order.deliveryOtp.isNotBlank()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (order.pickupOtp.isNotBlank()) {
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text("Pickup OTP: ${order.pickupOtp}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(8.dp))
                        }
                    }
                    if (order.deliveryOtp.isNotBlank()) {
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                            Text("Delivery OTP: ${order.deliveryOtp}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }

            AnimatedOrderStepper(
                steps = steps,
                currentStep = (order.statusStep - 1).coerceIn(0, steps.lastIndex)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (order.isLive) {
                    GradientPillButton(
                        text = "Simulate Next Step",
                        icon = Icons.Default.FastForward,
                        onClick = onAdvance,
                        modifier = Modifier.weight(1f),
                        testTagName = "advance_order_${order.id}"
                    )
                } else {
                    GradientPillButton(
                        text = "1-Tap Rebook",
                        icon = Icons.Default.Refresh,
                        onClick = onRebook,
                        modifier = Modifier.weight(1f),
                        testTagName = "rebook_order_${order.id}"
                    )
                }
                if (order.serviceType == ServiceType.GROCERY && !order.isRefundRequested) {
                    OutlinedButton(
                        onClick = onRefund,
                        modifier = Modifier.testTag("refund_order_${order.id}")
                    ) {
                        Text("Return/Refund", style = MaterialTheme.typography.labelSmall)
                    }
                } else if (order.isRefundRequested) {
                    StatusPill("Refund Credited", AmberWarning)
                }
            }
        }
    }
}
