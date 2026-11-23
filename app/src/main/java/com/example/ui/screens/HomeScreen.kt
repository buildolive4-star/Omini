package com.example.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
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
    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val serviceCards = remember {
        listOf(
            HomeServiceSpec(
                id = "ride",
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
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. TOP HEADER: Hamburger + Brand + Greeting + Avatar + Right Utilities
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Hamburger + Logo + Greeting
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GlassWhiteHigh,
                        border = BorderStroke(1.2.dp, SkyBlue.copy(alpha = 0.55f)),
                        shadowElevation = 6.dp
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
                                tint = DarkNavy
                            )
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_fg),
                        contentDescription = "SkyPulse Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.2.dp, PremiumPink, RoundedCornerShape(12.dp))
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SkyPulse",
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkNavy,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Hello, Welcome!",
                            style = MaterialTheme.typography.labelSmall,
                            color = PremiumPink,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Right: Wallet Balance, Notifications, Cart, Profile Avatar
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = GlassWhiteHigh,
                        border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { viewModel.navigateTo(AppRoute.Wallet) }
                            .testTag("header_wallet_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, null, tint = SkyBlue, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "₹${state.walletBalance}",
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkNavy,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    Box {
                        Surface(
                            shape = CircleShape,
                            color = GlassWhiteHigh,
                            border = BorderStroke(1.dp, LightPink),
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
                                Icon(Icons.Default.Notifications, "Notifications", tint = DarkNavy, modifier = Modifier.size(20.dp))
                            }
                        }
                        if (state.unreadNotificationsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(PremiumPink),
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
                            color = DarkNavy,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
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
                                    .background(SkyBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${state.totalCartItemsCount}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DarkNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Location Selector Bar + Avatar
                Glass3DCard(
                    cornerRadius = 18.dp,
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
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SkyToPinkBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MyLocation, "Detect Location", tint = PureWhite, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT LOCATION • TAP TO CHANGE",
                                style = MaterialTheme.typography.labelSmall,
                                color = SkyBlue,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.profile.currentLocation,
                                style = MaterialTheme.typography.titleSmall,
                                color = DarkNavy,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = MutedNavy)
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PalePink)
                                .border(1.dp, PremiumPink, CircleShape)
                                .clickable { viewModel.navigateTo(AppRoute.Profile) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.profile.avatarInitials,
                                style = MaterialTheme.typography.labelMedium,
                                color = DarkNavy,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // Search Bar + Voice Search + Gemini AI Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.globalSearchQuery,
                        onValueChange = viewModel::updateSearchQuery,
                        placeholder = { Text("Search rides, pizza, milk, parcel…", style = MaterialTheme.typography.bodyMedium) },
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
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GlassWhiteHigh,
                            unfocusedContainerColor = GlassWhiteHigh,
                            focusedBorderColor = SkyBlue,
                            unfocusedBorderColor = LightSkyBlue.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("home_search_input")
                    )

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color.Transparent,
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = PremiumPink)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SkyToPinkBrush)
                            .clickable { viewModel.navigateTo(AppRoute.AiAssistant) }
                            .testTag("home_ai_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, "SkyPulse Gemini AI", tint = PureWhite, modifier = Modifier.size(26.dp))
                        }
                    }
                }
            }
        }

        // 2. EXTRA PREMIUM FEATURE #5: LIVE ACTIVITY CARD ON HOME SCREEN
        state.liveOrder?.let { live ->
            item {
                Glass3DCard(
                    gradientBrush = NavyGlowBrush,
                    cornerRadius = 24.dp,
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
                            StatusPill("LIVE ACTIVITY • ${live.serviceType.label.uppercase()}", EmeraldSuccess)
                            Text(
                                text = live.etaText,
                                style = MaterialTheme.typography.labelMedium,
                                color = LightSkyBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = live.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = PureWhite
                        )
                        Text(
                            text = "${live.status} • ${live.subtitle}",
                            style = MaterialTheme.typography.bodySmall,
                            color = LightPink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        LinearProgressIndicator(
                            progress = { (live.statusStep.toFloat() / live.totalSteps.coerceAtLeast(1)).coerceIn(0.1f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(CircleShape),
                            color = SkyBlue,
                            trackColor = PureWhite.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }

        // 3. PROMO BANNER FROM ADMIN CONFIG
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = PalePink.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, PremiumPink.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(AppRoute.MultiCart) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = PremiumPink, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = state.adminConfig.bannerHeadline,
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. SECTION TITLE + 5 LARGE ANIMATED 3D SERVICE CARDS
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
                        color = DarkNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Tap any 3D module to launch live booking",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedNavy
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

        // 5. EXTRA PREMIUM FEATURE #1: ONE-TAP REBOOKING STRIP
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "One-Tap Rebooking",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkNavy
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.orders.take(4), key = { "rebook_${it.id}" }) { order ->
                        Glass3DCard(
                            modifier = Modifier.width(230.dp),
                            cornerRadius = 20.dp,
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusPill(order.serviceType.label, SkyBlue)
                                    Text("₹${order.amount}", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                }
                                Text(
                                    text = order.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = DarkNavy,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = order.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedNavy,
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

        // 6. EXTRA PREMIUM FEATURES #3 & #4: FAMILY ACCOUNT & BUSINESS ACCOUNT QUICK ACCESS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Glass3DCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 22.dp,
                    onClick = { viewModel.navigateTo(AppRoute.CustomerDetail(CustomerSection.FAMILY_ACCOUNT)) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Groups, null, tint = SkyBlue, modifier = Modifier.size(28.dp))
                        Text("Family Account", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                        Text(
                            "${state.familyMembers.size} profiles & saved addresses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedNavy
                        )
                    }
                }
                Glass3DCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 22.dp,
                    onClick = { viewModel.navigateTo(AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD)) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.BusinessCenter, null, tint = PremiumPink, modifier = Modifier.size(28.dp))
                        Text("Business B2B", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                        Text(
                            "Bulk parcel & truck booking",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedNavy
                        )
                    }
                }
            }
        }

        // 7. PROTOTYPE DISCLAIMER FOOTER
        item {
            Text(
                text = "Interactive Super App Prototype • Simulated bookings, payments & live tracking. AI powered by Gemini.",
                style = MaterialTheme.typography.bodySmall,
                color = MutedNavy,
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
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )

    Glass3DCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("service_card_${spec.id}"),
        cornerRadius = 28.dp,
        gradientBrush = spec.gradient,
        elevation = 14.dp,
        onClick = onClick,
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.15f)) {
                    Surface(
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.28f)
                    ) {
                        Text(
                            text = "3D HYPERLOCAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = spec.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = PureWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = spec.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PureWhite.copy(alpha = 0.92f)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(width = 128.dp, height = 98.dp)
                        .graphicsLayer { translationY = floatY }
                        .shadow(12.dp, RoundedCornerShape(22.dp))
                        .clip(RoundedCornerShape(22.dp))
                        .border(2.dp, PureWhite.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
                ) {
                    Image(
                        painter = painterResource(id = spec.imageRes),
                        contentDescription = "${spec.title} 3D illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Options horizontal chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                spec.options.forEach { opt ->
                    Surface(
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.24f),
                        border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = opt,
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Animated CTA Button
            Surface(
                shape = RoundedCornerShape(50),
                color = PureWhite,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onClick)
                    .testTag("cta_${spec.id}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = spec.ctaLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = DarkNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("services_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("All Services & Panels", style = MaterialTheme.typography.headlineMedium, color = DarkNavy)
                    Text("Unified customer, partner, merchant & admin access", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                }
                Surface(
                    shape = CircleShape,
                    color = GlassWhiteHigh,
                    border = BorderStroke(1.dp, SkyBlue)
                ) {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, "Open Drawer", tint = DarkNavy)
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
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(SkyToPinkBrush),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, null, tint = PureWhite)
                                    }
                                    Text(title, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(4.dp))
            Text("Switch Inside-App Role Panels", style = MaterialTheme.typography.titleLarge, color = DarkNavy)
            Text("Requires role authorization PIN for Partner, Merchant, Business & Admin consoles", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
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
                        cornerRadius = 20.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                Text(sub, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            }
                            StatusPill("OPEN", PremiumPink)
                        }
                    }
                }
            }
        }
    }
}
