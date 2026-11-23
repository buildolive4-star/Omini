package com.example.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AppRoute
import com.example.data.CustomerSection
import com.example.data.DemoDataSeeder
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.CardCoralPinkGradient
import com.example.ui.theme.CardIndigoSkyGradient
import com.example.ui.theme.CardMintSkyGradient
import com.example.ui.theme.CardPinkGradient
import com.example.ui.theme.CardSkyGradient
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassDarkHigh
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.LocalIsDarkGlass
import com.example.ui.theme.MutedLightBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.NavyGlowBrush
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyToPinkBrush
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

private data class HomeServiceSpec(
    val id: String,
    val tag: String,
    val title: String,
    val subtitle: String,
    val options: List<String>,
    val ctaLabel: String,
    @DrawableRes val imageRes: Int,
    val gradient: Brush,
    val route: AppRoute
)

@Composable
fun HomeScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel,
    onOpenDrawer: () -> Unit,
    onVoiceSearchClick: () -> Unit
) {
    val isDark = LocalIsDarkGlass.current
    val primaryText = if (isDark) PureWhite else DarkNavy
    val secondaryText = if (isDark) MutedLightBlue else MutedNavy
    val pillBg = if (isDark) GlassDarkHigh else GlassWhiteHigh

    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val serviceCards = remember {
        listOf(
            HomeServiceSpec(
                id = "ride",
                tag = "2-MIN PICKUP",
                title = "Ride",
                subtitle = "Travel anywhere, anytime.",
                options = listOf("Bike Taxi", "Auto", "Mini Cab", "Sedan", "SUV"),
                ctaLabel = "BOOK A RIDE",
                imageRes = R.drawable.img_service_ride,
                gradient = CardSkyGradient,
                route = AppRoute.Ride
            ),
            HomeServiceSpec(
                id = "food",
                tag = "HOT & FRESH",
                title = "Food Delivery",
                subtitle = "Delicious food at your doorstep.",
                options = listOf("Restaurants", "Fast Food", "Pizza", "Biryani", "Desserts"),
                ctaLabel = "ORDER FOOD",
                imageRes = R.drawable.img_service_food,
                gradient = CardPinkGradient,
                route = AppRoute.Food
            ),
            HomeServiceSpec(
                id = "grocery",
                tag = "15-MIN EXPRESS",
                title = "Grocery",
                subtitle = "Daily essentials delivered.",
                options = listOf("Vegetables", "Fruits", "Dairy", "Snacks", "Household"),
                ctaLabel = "SHOP NOW",
                imageRes = R.drawable.img_service_grocery,
                gradient = CardMintSkyGradient,
                route = AppRoute.Grocery
            ),
            HomeServiceSpec(
                id = "parcel",
                tag = "INSURED • OTP",
                title = "Send Parcel",
                subtitle = "Send anything safely.",
                options = listOf("Documents", "Small Packages", "Gifts", "Business Parcels"),
                ctaLabel = "SEND PARCEL",
                imageRes = R.drawable.img_service_parcel,
                gradient = CardCoralPinkGradient,
                route = AppRoute.Parcel
            ),
            HomeServiceSpec(
                id = "transport",
                tag = "MINI TO 19FT TRUCK",
                title = "Transport",
                subtitle = "Move your goods easily.",
                options = listOf("Two Wheeler", "Three Wheeler", "Tata Ace", "Pickup", "Mini Truck", "Large Truck"),
                ctaLabel = "BOOK TRANSPORT",
                imageRes = R.drawable.img_service_transport,
                gradient = CardIndigoSkyGradient,
                route = AppRoute.Transport
            )
        )
    }

    val filteredServices = remember(state.globalSearchQuery, serviceCards) {
        val q = state.globalSearchQuery.trim()
        if (q.isEmpty()) serviceCards
        else serviceCards.filter { s ->
            s.title.contains(q, true) || s.subtitle.contains(q, true) || s.options.any { it.contains(q, true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 116.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. ULTRA-POLISHED 3D GLASS TOP HEADER
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = pillBg,
                        border = BorderStroke(1.3.dp, SkyToPinkBrush),
                        shadowElevation = 8.dp
                    ) {
                        IconButton(
                            onClick = onOpenDrawer,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("hamburger_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Navigation Menu",
                                tint = primaryText
                            )
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_fg),
                        contentDescription = "SkyPulse Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(8.dp, RoundedCornerShape(13.dp), spotColor = ElectricPink)
                            .clip(RoundedCornerShape(13.dp))
                            .border(1.4.dp, SkyToPinkBrush, RoundedCornerShape(13.dp))
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SkyPulse",
                            style = MaterialTheme.typography.titleLarge,
                            color = primaryText,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Hello, Welcome!",
                            style = MaterialTheme.typography.labelSmall,
                            color = PremiumPink,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // 1-Tap Pearl Light / Cyber Midnight Glass Theme Toggle
                    Surface(
                        shape = CircleShape,
                        color = pillBg,
                        border = BorderStroke(1.2.dp, SkyBlue.copy(alpha = 0.65f)),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.toggleDarkGlassTheme() }
                            .testTag("theme_toggle_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle 3D Glass Theme",
                                tint = if (isDark) CyberCyan else PremiumPink,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    Box {
                        Surface(
                            shape = CircleShape,
                            color = pillBg,
                            border = BorderStroke(1.2.dp, LightPink),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    showNotificationsDialog = true
                                    viewModel.markAllNotificationsRead()
                                }
                                .testTag("header_notification_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Notifications, "Notifications", tint = primaryText, modifier = Modifier.size(19.dp))
                            }
                        }
                        if (state.unreadNotificationsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(ElectricPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${state.unreadNotificationsCount}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PureWhite
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    Box {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SkyToPinkBrush)
                                .clickable { viewModel.navigateTo(AppRoute.MultiCart) }
                                .testTag("header_cart_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ShoppingCart, "Multi-Service Cart", tint = PureWhite, modifier = Modifier.size(19.dp))
                            }
                        }
                        if (state.totalCartItemsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(17.dp)
                                    .clip(CircleShape)
                                    .background(DarkNavy)
                                    .border(1.dp, PureWhite, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${state.totalCartItemsCount}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                // Location + Wallet Glass Pill Bar
                Glass3DCard(
                    cornerRadius = 22.dp,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    onClick = { showLocationDialog = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("location_selector_bar"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .shadow(6.dp, CircleShape, spotColor = ElectricPink)
                                .clip(CircleShape)
                                .background(SkyToPinkBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MyLocation, "Detect Location", tint = PureWhite, modifier = Modifier.size(19.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT LOCATION • TAP TO CHANGE",
                                style = MaterialTheme.typography.labelSmall,
                                color = SkyBlue,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = state.profile.currentLocation,
                                style = MaterialTheme.typography.titleSmall,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = secondaryText)
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isDark) Color(0xFF1A3264) else PaleSky,
                            border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { viewModel.navigateTo(AppRoute.Wallet) }
                                .testTag("header_wallet_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, null, tint = SkyBlue, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "₹${state.walletBalance}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = primaryText,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                // Glowing Glass Search Bar + Voice Search + Gemini AI Sparkle Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = state.globalSearchQuery,
                        onValueChange = viewModel::updateSearchQuery,
                        placeholder = {
                            Text(
                                "Search rides, pizza, milk, parcel…",
                                style = MaterialTheme.typography.bodyMedium,
                                color = secondaryText
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = SkyBlue) },
                        trailingIcon = {
                            IconButton(
                                onClick = onVoiceSearchClick,
                                modifier = Modifier.testTag("voice_search_button")
                            ) {
                                Icon(Icons.Default.Mic, "Voice Search", tint = PremiumPink)
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = pillBg,
                            unfocusedContainerColor = pillBg,
                            focusedTextColor = primaryText,
                            unfocusedTextColor = primaryText,
                            focusedBorderColor = SkyBlue,
                            unfocusedBorderColor = SkyBlue.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("home_search_input")
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent,
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = ElectricPink, ambientColor = SkyBlue)
                            .clip(RoundedCornerShape(20.dp))
                            .background(SkyToPinkBrush)
                            .border(1.4.dp, PureWhite.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                            .clickable { viewModel.navigateTo(AppRoute.AiAssistant) }
                            .testTag("home_ai_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, "SkyPulse Gemini AI", tint = PureWhite, modifier = Modifier.size(27.dp))
                        }
                    }
                }
            }
        }

        // 2. LIVE ACTIVITY CARD WITH PULSING RADAR BEACON
        state.liveOrder?.let { live ->
            item {
                val beaconInfinite = rememberInfiniteTransition(label = "live_beacon")
                val beaconAlpha by beaconInfinite.animateFloat(
                    initialValue = 0.35f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
                    label = "beacon_alpha"
                )
                Glass3DCard(
                    gradientBrush = NavyGlowBrush,
                    cornerRadius = 26.dp,
                    elevation = 16.dp,
                    onClick = { viewModel.navigateTo(AppRoute.Orders) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("live_activity_card"),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSuccess.copy(alpha = beaconAlpha))
                                )
                                StatusPill("LIVE • ${live.serviceType.label.uppercase()}", EmeraldSuccess)
                            }
                            Text(
                                text = live.etaText,
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(
                            text = live.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = PureWhite,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${live.status} • ${live.subtitle}",
                            style = MaterialTheme.typography.bodySmall,
                            color = LightPink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        LinearProgressIndicator(
                            progress = { (live.statusStep.toFloat() / live.totalSteps.coerceAtLeast(1)).coerceIn(0.12f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = CyberCyan,
                            trackColor = PureWhite.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }

        // 3. LUMINOUS PROMO RIBBON
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isDark) Color(0x993E1448) else PalePink.copy(alpha = 0.9f),
                border = BorderStroke(1.2.dp, SkyToPinkBrush),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(AppRoute.MultiCart) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = ElectricPink, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = state.adminConfig.bannerHeadline,
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryText,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. 5 LARGE ULTRA-3D ANIMATED SERVICE CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "All Services in One Super App",
                        style = MaterialTheme.typography.headlineSmall,
                        color = primaryText,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Sky Blue + Pink 3D Glass Experience",
                        style = MaterialTheme.typography.bodySmall,
                        color = secondaryText
                    )
                }
                StatusPill("5 MODULES", SkyBlue)
            }
        }

        items(filteredServices, key = { it.id }) { spec ->
            Animated3DServiceCard(
                spec = spec,
                onClick = { viewModel.navigateTo(spec.route) }
            )
        }

        // 5. ONE-TAP REBOOKING STRIP
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "One-Tap Rebooking",
                    style = MaterialTheme.typography.titleLarge,
                    color = primaryText,
                    fontWeight = FontWeight.ExtraBold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.orders.take(4), key = { "rebook_${it.id}" }) { order ->
                        Glass3DCard(
                            modifier = Modifier.width(236.dp),
                            cornerRadius = 22.dp,
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusPill(order.serviceType.label, SkyBlue)
                                    Text("₹${order.amount}", style = MaterialTheme.typography.titleMedium, color = primaryText, fontWeight = FontWeight.ExtraBold)
                                }
                                Text(
                                    text = order.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = primaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = order.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(2.dp))
                                GradientPillButton(
                                    text = "Repeat in 1 Tap",
                                    icon = Icons.Default.Refresh,
                                    onClick = { viewModel.oneTapRebook(order) },
                                    modifier = Modifier.fillMaxWidth(),
                                    testTagName = "rebook_${order.id}"
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. FAMILY & BUSINESS QUICK CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Glass3DCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 24.dp,
                    onClick = { viewModel.navigateTo(AppRoute.CustomerDetail(CustomerSection.FAMILY_ACCOUNT)) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Groups, null, tint = SkyBlue, modifier = Modifier.size(30.dp))
                        Text("Family Account", style = MaterialTheme.typography.titleMedium, color = primaryText)
                        Text(
                            "${state.familyMembers.size} profiles & saved addresses",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryText
                        )
                    }
                }
                Glass3DCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 24.dp,
                    onClick = { viewModel.navigateTo(AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD)) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.BusinessCenter, null, tint = PremiumPink, modifier = Modifier.size(30.dp))
                        Text("Business B2B", style = MaterialTheme.typography.titleMedium, color = primaryText)
                        Text(
                            "Bulk parcel & truck booking",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryText
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Interactive Super App Prototype • Simulated bookings, payments & live tracking. AI powered by Gemini.",
                style = MaterialTheme.typography.bodySmall,
                color = secondaryText,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }

    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Select Delivery & Pickup Location") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DemoDataSeeder.popularLocations.forEach { loc ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (loc == state.profile.currentLocation) PaleSky else SoftBackgroundDialog,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateLocation(loc)
                                    showLocationDialog = false
                                }
                        ) {
                            Text(
                                text = loc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkNavy,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) { Text("Close") }
            }
        )
    }

    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("Notifications") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.notifications.forEach { n ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = PaleSky.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(n.title, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                Text(n.body, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                Text(n.time, style = MaterialTheme.typography.labelSmall, color = PremiumPink)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) { Text("Done") }
            }
        )
    }
}

private val SoftBackgroundDialog = Color(0xFFF5FAFF)

@Composable
private fun Animated3DServiceCard(
    spec: HomeServiceSpec,
    onClick: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "float_${spec.id}")
    val floatY by infinite.animateFloat(
        initialValue = -4.5f,
        targetValue = 4.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )
    val tiltZ by infinite.animateFloat(
        initialValue = -1.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_tilt"
    )

    Glass3DCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("service_card_${spec.id}"),
        cornerRadius = 30.dp,
        gradientBrush = spec.gradient,
        elevation = 18.dp,
        onClick = onClick,
        contentPadding = PaddingValues(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.15f)) {
                    Surface(
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.24f),
                        border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.55f))
                    ) {
                        Text(
                            text = spec.tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = spec.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = PureWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = spec.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PureWhite.copy(alpha = 0.94f)
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Floating 3D artwork with luminous white halo ring
                Box(
                    modifier = Modifier
                        .size(width = 136.dp, height = 106.dp)
                        .graphicsLayer {
                            translationY = floatY
                            rotationZ = tiltZ
                        }
                        .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = DarkNavy)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.2.dp, PureWhite.copy(alpha = 0.92f), RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = spec.imageRes),
                        contentDescription = "${spec.title} 3D illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Frosted Glass Sub-option Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                spec.options.forEach { opt ->
                    Surface(
                        shape = CircleShape,
                        color = DarkNavy.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = opt,
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Gleaming Glass-White 3D CTA Bar
            Surface(
                shape = RoundedCornerShape(50),
                color = PureWhite.copy(alpha = 0.96f),
                shadowElevation = 10.dp,
                border = BorderStroke(1.dp, PureWhite),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onClick)
                    .testTag("cta_${spec.id}")
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = spec.ctaLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = DarkNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(SkyToPinkBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServicesDirectoryScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel,
    onOpenDrawer: () -> Unit
) {
    val isDark = LocalIsDarkGlass.current
    val primaryText = if (isDark) PureWhite else DarkNavy
    val secondaryText = if (isDark) MutedLightBlue else MutedNavy

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("services_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 116.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("All Services & Panels", style = MaterialTheme.typography.headlineMedium, color = primaryText)
                    Text("Unified customer, partner, merchant & admin access", style = MaterialTheme.typography.bodySmall, color = secondaryText)
                }
                Surface(
                    shape = CircleShape,
                    color = if (isDark) GlassDarkHigh else GlassWhiteHigh,
                    border = BorderStroke(1.2.dp, SkyBlue)
                ) {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, "Open Drawer", tint = primaryText)
                    }
                }
            }
        }

        item {
            val quickModules = listOf(
                Triple("Ride Booking", Icons.Default.DirectionsBike, AppRoute.Ride),
                Triple("Food Delivery", Icons.Default.Fastfood, AppRoute.Food),
                Triple("Grocery Mart", Icons.Default.ShoppingBasket, AppRoute.Grocery),
                Triple("Send Parcel", Icons.Default.Inventory2, AppRoute.Parcel),
                Triple("Goods Transport", Icons.Default.LocalShipping, AppRoute.Transport),
                Triple("Gemini AI Hub", Icons.Default.AutoAwesome, AppRoute.AiAssistant)
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                quickModules.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        pair.forEach { (title, icon, route) ->
                            Glass3DCard(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.navigateTo(route) }
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(15.dp))
                                            .background(SkyToPinkBrush),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, null, tint = PureWhite)
                                    }
                                    Text(title, style = MaterialTheme.typography.titleMedium, color = primaryText)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(4.dp))
            Text("Switch Inside-App Role Panels", style = MaterialTheme.typography.titleLarge, color = primaryText)
            Text("Requires role authorization PIN for Partner, Merchant, Business & Admin consoles", style = MaterialTheme.typography.bodySmall, color = secondaryText)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("Driver / Delivery Partner Dashboard", "Accept category-matched live jobs & track payouts", AppRoute.PartnerDashboard),
                    Triple("Become a Partner (KYC Onboarding)", "Register Aadhaar, PAN, DL, RC & Bank details", AppRoute.PartnerRegistration),
                    Triple("Restaurant Owner Panel", "Live kitchen orders, menu editor & revenue stats", AppRoute.RestaurantPanel),
                    Triple("Grocery Store Owner Panel", "Inventory stock updates, packing & settlements", AppRoute.ShopPanel),
                    Triple("Business B2B Account", "Bulk parcels, fleet transport & GST credit", AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD)),
                    Triple("Super Admin Console", "Platform KPIs, dynamic surge pricing & AI fraud monitor", AppRoute.AdminPanel)
                ).forEach { (title, sub, route) ->
                    Glass3DCard(
                        onClick = { viewModel.navigateTo(route) },
                        cornerRadius = 22.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, style = MaterialTheme.typography.titleMedium, color = primaryText)
                                Text(sub, style = MaterialTheme.typography.bodySmall, color = secondaryText)
                            }
                            StatusPill("OPEN", PremiumPink)
                        }
                    }
                }
            }
        }
    }
}
