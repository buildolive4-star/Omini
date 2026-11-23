package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AppRoute
import com.example.data.CustomerSection
import com.example.data.UserRole
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.LightPink
import com.example.ui.theme.LightSkyBlue
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyToPinkBrush
import com.example.viewmodel.SuperAppUiState

private data class DrawerMenuItem(
    val number: Int,
    val label: String,
    val icon: ImageVector,
    val badge: String? = null,
    val lockedRole: UserRole? = null,
    val testTag: String,
    val onClick: () -> Unit
)

@Composable
fun SkyPulseGlassDrawer(
    state: SuperAppUiState,
    onNavigate: (AppRoute) -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val customerItems = listOf(
        DrawerMenuItem(1, "Home", Icons.Default.Home, null, null, "drawer_home") {
            onNavigate(AppRoute.Home)
        },
        DrawerMenuItem(2, "My Profile", Icons.Default.Person, null, null, "drawer_profile") {
            onNavigate(AppRoute.Profile)
        },
        DrawerMenuItem(3, "My Rides", Icons.Default.DirectionsCar, null, null, "drawer_rides") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_RIDES))
        },
        DrawerMenuItem(4, "My Food Orders", Icons.Default.Fastfood, null, null, "drawer_food") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_FOOD_ORDERS))
        },
        DrawerMenuItem(5, "My Grocery Orders", Icons.Default.ShoppingBasket, null, null, "drawer_grocery") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_GROCERY_ORDERS))
        },
        DrawerMenuItem(6, "My Parcel Bookings", Icons.Default.Inventory2, null, null, "drawer_parcel") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_PARCEL_BOOKINGS))
        },
        DrawerMenuItem(7, "My Transport Bookings", Icons.Default.LocalShipping, null, null, "drawer_transport") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_TRANSPORT_BOOKINGS))
        },
        DrawerMenuItem(8, "My Wallet", Icons.Default.AccountBalanceWallet, "₹${state.walletBalance}", null, "drawer_wallet") {
            onNavigate(AppRoute.Wallet)
        },
        DrawerMenuItem(9, "My Payments", Icons.Default.CreditCard, null, null, "drawer_payments") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_PAYMENTS))
        },
        DrawerMenuItem(10, "My Coupons", Icons.Default.ConfirmationNumber, "${state.coupons.size}", null, "drawer_coupons") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_COUPONS))
        },
        DrawerMenuItem(11, "My Addresses", Icons.Default.LocationOn, null, null, "drawer_addresses") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_ADDRESSES))
        },
        DrawerMenuItem(12, "My Favourite", Icons.Default.Favorite, null, null, "drawer_favourite") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_FAVOURITES))
        },
        DrawerMenuItem(13, "My Reviews", Icons.Default.Star, null, null, "drawer_reviews") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.MY_REVIEWS))
        },
        DrawerMenuItem(14, "Help & Support", Icons.AutoMirrored.Filled.HelpOutline, "AI", null, "drawer_support") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.HELP_SUPPORT))
        },
        DrawerMenuItem(15, "Settings", Icons.Default.Settings, null, null, "drawer_settings") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.SETTINGS))
        },
        DrawerMenuItem(16, "Logout", Icons.AutoMirrored.Filled.ExitToApp, null, null, "drawer_logout") {
            onLogout()
        }
    )

    val otherPanels = listOf(
        DrawerMenuItem(17, "Become a Driver / Delivery Partner", Icons.Default.TwoWheeler, "KYC", null, "drawer_become_partner") {
            onNavigate(AppRoute.PartnerRegistration)
        },
        DrawerMenuItem(18, "Partner Dashboard", Icons.Default.Dashboard, "LIVE", UserRole.DRIVER_PARTNER, "drawer_partner_dashboard") {
            onNavigate(AppRoute.PartnerDashboard)
        },
        DrawerMenuItem(19, "Restaurant Owner Panel", Icons.Default.Restaurant, "PRO", UserRole.RESTAURANT_OWNER, "drawer_restaurant_panel") {
            onNavigate(AppRoute.RestaurantPanel)
        },
        DrawerMenuItem(20, "Grocery Store Owner Panel", Icons.Default.Storefront, "PRO", UserRole.GROCERY_OWNER, "drawer_shop_panel") {
            onNavigate(AppRoute.ShopPanel)
        },
        DrawerMenuItem(21, "Business Dashboard", Icons.Default.BusinessCenter, "B2B", UserRole.BUSINESS_ACCOUNT, "drawer_business_panel") {
            onNavigate(AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD))
        },
        DrawerMenuItem(22, "Admin Login", Icons.Default.AdminPanelSettings, "AUTH", UserRole.ADMIN, "drawer_admin_panel") {
            onNavigate(AppRoute.AdminPanel)
        }
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.Transparent,
        modifier = Modifier
            .width(326.dp)
            .fillMaxHeight()
            .testTag("navigation_drawer")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xF5E0F2FE),
                            Color(0xF5FFFFFF),
                            Color(0xF5FCE7F3)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = SkyToPinkBrush,
                    shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    // Drawer 3D Glass Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Brush.linearGradient(listOf(DarkNavy, Color(0xFF1E3A8A), Color(0xFF701A75))))
                            .border(1.dp, PureWhite.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_app_icon_fg),
                                    contentDescription = "SkyPulse Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.5.dp, SkyBlue, RoundedCornerShape(14.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = state.profile.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = PureWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Active Role: ${state.activeRole.title}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LightSkyBlue
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = SkyBlue.copy(alpha = 0.25f),
                                    modifier = Modifier.clickable {
                                        onCloseDrawer()
                                        onNavigate(AppRoute.CustomerDetail(CustomerSection.FAMILY_ACCOUNT))
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Groups, null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Family (${state.familyMembers.size})", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = PremiumPink.copy(alpha = 0.3f),
                                    modifier = Modifier.clickable {
                                        onCloseDrawer()
                                        onNavigate(AppRoute.AiAssistant)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Gemini AI", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "CUSTOMER SECTION",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedNavy,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                items(customerItems, key = { it.number }) { item ->
                    DrawerRowCard(
                        item = item,
                        isUnlocked = true,
                        onTap = {
                            onCloseDrawer()
                            item.onClick()
                        }
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = LightSkyBlue.copy(alpha = 0.5f))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "PARTNER, MERCHANT & ADMIN PANELS",
                        style = MaterialTheme.typography.labelSmall,
                        color = PremiumPink,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                items(otherPanels, key = { it.number }) { item ->
                    val unlocked = item.lockedRole == null || item.lockedRole in state.unlockedRoles
                    DrawerRowCard(
                        item = item,
                        isUnlocked = unlocked,
                        onTap = {
                            onCloseDrawer()
                            item.onClick()
                        }
                    )
                }

                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
private fun DrawerRowCard(
    item: DrawerMenuItem,
    isUnlocked: Boolean,
    onTap: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onTap)
            .testTag(item.testTag),
        shape = RoundedCornerShape(16.dp),
        color = if (item.lockedRole != null) PalePink.copy(alpha = 0.45f) else GlassWhiteHigh.copy(alpha = 0.78f),
        border = BorderStroke(
            width = 1.dp,
            color = if (item.lockedRole != null) LightPink.copy(alpha = 0.7f) else PaleSky
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (item.lockedRole != null) SkyToPinkBrush
                        else Brush.linearGradient(listOf(PaleSky, PalePink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (item.lockedRole != null) PureWhite else DarkNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "${item.number}. ${item.label}",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkNavy,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (item.lockedRole != null) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = if (isUnlocked) "Unlocked" else "Requires Login",
                    tint = if (isUnlocked) EmeraldSuccess else PremiumPink,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            if (item.badge != null) {
                Surface(
                    shape = CircleShape,
                    color = if (item.lockedRole != null) DarkNavy else SkyBlue.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = item.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.lockedRole != null) PureWhite else DarkNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

private data class BottomNavTab(
    val route: AppRoute,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

/**
 * Floating 3D Glassmorphism Bottom Navigation Bar with safe navigationBarsPadding.
 */
@Composable
fun FloatingGlassBottomBar(
    currentRoute: AppRoute,
    liveOrdersCount: Int,
    onSelect: (AppRoute) -> Unit
) {
    val tabs = listOf(
        BottomNavTab(AppRoute.Home, "Home", Icons.Default.Home, "nav_home"),
        BottomNavTab(AppRoute.Services, "Services", Icons.Default.GridView, "nav_services"),
        BottomNavTab(AppRoute.Orders, "Orders", Icons.Default.ReceiptLong, "nav_orders"),
        BottomNavTab(AppRoute.Wallet, "Wallet", Icons.Default.AccountBalanceWallet, "nav_wallet"),
        BottomNavTab(AppRoute.Profile, "Profile", Icons.Default.Person, "nav_profile")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = SkyBlue.copy(alpha = 0.35f),
                    spotColor = PremiumPink.copy(alpha = 0.35f)
                ),
            shape = RoundedCornerShape(30.dp),
            color = DarkNavy.copy(alpha = 0.94f),
            border = BorderStroke(1.2.dp, SkyToPinkBrush)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.08f else 0.96f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "nav_scale"
                    )
                    val tint by animateColorAsState(
                        targetValue = if (selected) PureWhite else LightSkyBlue.copy(alpha = 0.72f),
                        label = "nav_tint"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                if (selected) SkyToPinkBrush
                                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                            .clickable { onSelect(tab.route) }
                            .padding(vertical = 8.dp)
                            .testTag(tab.tag),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    tint = tint,
                                    modifier = Modifier.size(22.dp)
                                )
                                if (tab.route == AppRoute.Orders && liveOrdersCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(9.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldSuccess)
                                            .border(1.dp, PureWhite, CircleShape)
                                    )
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = tint,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
