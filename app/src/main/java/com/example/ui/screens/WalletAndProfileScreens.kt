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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppRoute
import com.example.data.CustomerSection
import com.example.data.ServiceType
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardIndigoSkyGradient
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
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
import com.example.ui.theme.SkyToPinkBrush
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

@Composable
fun WalletScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    var customAmount by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wallet_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("SkyPulse Fintech Wallet", style = MaterialTheme.typography.headlineMedium, color = DarkNavy)
            Text("Instant top-ups, cashback refunds & unified payments", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
        }

        // 3D Glass Wallet Hero Card
        item {
            Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 28.dp, elevation = 14.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill("VERIFIED WALLET", SkyBlue)
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = PremiumPink, modifier = Modifier.size(28.dp))
                    }
                    Text("Available Balance", style = MaterialTheme.typography.labelMedium, color = LightSkyBlue)
                    Text(
                        text = "₹${state.walletBalance}.00",
                        style = MaterialTheme.typography.displayLarge,
                        color = PureWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Promo Credits: ₹150", style = MaterialTheme.typography.bodySmall, color = LightPink)
                        Text("• Referral Earned: ₹${state.profile.referralEarned}", style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                    }
                }
            }
        }

        // Quick Add Money
        item {
            Glass3DCard(cornerRadius = 22.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Add Money Instantly (Simulated)", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listOf(200, 500, 1000, 2000)) { amt ->
                            GlassChip(
                                text = "+ ₹$amt",
                                selected = false,
                                onClick = { viewModel.addMoneyToWallet(amt) }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = customAmount,
                            onValueChange = { customAmount = it.filter(Char::isDigit) },
                            placeholder = { Text("Custom ₹ amount") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("wallet_custom_amount")
                        )
                        GradientPillButton(
                            text = "Add",
                            icon = Icons.Default.Add,
                            onClick = {
                                val amt = customAmount.toIntOrNull() ?: 0
                                if (amt > 0) {
                                    viewModel.addMoneyToWallet(amt)
                                    customAmount = ""
                                }
                            },
                            testTagName = "wallet_add_button"
                        )
                    }
                }
            }
        }

        // Saved Payment Methods
        item {
            Glass3DCard(cornerRadius = 22.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Payment Methods", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                    listOf("SkyPulse Wallet", "UPI (aarav@okhdfcbank)", "Card •••• 4092", "Net Banking", "Cash where supported").forEach { pm ->
                        val active = state.selectedPaymentMethod == pm
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (active) PaleSky else PureWhite,
                            border = BorderStroke(1.dp, if (active) SkyBlue else PaleSky),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectPaymentMethod(pm) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CreditCard, null, tint = if (active) SkyBlue else MutedNavy)
                                Spacer(Modifier.width(10.dp))
                                Text(pm, style = MaterialTheme.typography.bodyMedium, color = DarkNavy, modifier = Modifier.weight(1f))
                                if (active) StatusPill("DEFAULT", EmeraldSuccess)
                            }
                        }
                    }
                }
            }
        }

        // Transactions
        item {
            Text("Transaction History", style = MaterialTheme.typography.titleLarge, color = DarkNavy)
        }

        items(state.transactions, key = { it.id }) { tx ->
            Glass3DCard(cornerRadius = 18.dp, contentPadding = PaddingValues(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tx.title, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                        Text("${tx.category} • ${tx.timestamp}", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${if (tx.isCredit) "+" else "-"}₹${tx.amount}",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (tx.isCredit) EmeraldSuccess else RoseError,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(tx.status, style = MaterialTheme.typography.labelSmall, color = MutedNavy)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 26.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SkyToPinkBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.profile.avatarInitials, style = MaterialTheme.typography.headlineSmall, color = PureWhite, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(state.profile.name, style = MaterialTheme.typography.titleLarge, color = PureWhite)
                        Text(state.profile.phone, style = MaterialTheme.typography.bodySmall, color = LightSkyBlue)
                        Text(state.profile.email, style = MaterialTheme.typography.bodySmall, color = LightPink)
                    }
                    Surface(
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.18f),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { showEditDialog = true }
                            .testTag("edit_profile_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Edit, "Edit Profile", tint = PureWhite)
                        }
                    }
                }
            }
        }

        // Referral & Rewards
        item {
            Glass3DCard(cornerRadius = 22.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Referral Code: ${state.profile.referralCode}", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                        Text("Invite friends & earn ₹100 • Loyalty: ${state.profile.loyaltyPoints} pts", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                    }
                    StatusPill("₹${state.profile.referralEarned} Won", EmeraldSuccess)
                }
            }
        }

        // Quick Customer Sections Grid
        item {
            val links = listOf(
                Triple("Family Account", Icons.Default.Groups, CustomerSection.FAMILY_ACCOUNT),
                Triple("Business B2B", Icons.Default.BusinessCenter, CustomerSection.BUSINESS_DASHBOARD),
                Triple("Saved Addresses", Icons.Default.LocationOn, CustomerSection.MY_ADDRESSES),
                Triple("My Coupons", Icons.Default.ConfirmationNumber, CustomerSection.MY_COUPONS),
                Triple("My Favourites", Icons.Default.Favorite, CustomerSection.MY_FAVOURITES),
                Triple("My Reviews", Icons.Default.Star, CustomerSection.MY_REVIEWS),
                Triple("Help & Support", Icons.Default.AutoAwesome, CustomerSection.HELP_SUPPORT),
                Triple("Settings", Icons.Default.CreditCard, CustomerSection.SETTINGS)
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                links.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        pair.forEach { (label, icon, section) ->
                            Glass3DCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 20.dp,
                                onClick = { viewModel.navigateTo(AppRoute.CustomerDetail(section)) }
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(icon, null, tint = PremiumPink, modifier = Modifier.size(22.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(label, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        var name by remember { mutableStateOf(state.profile.name) }
        var phone by remember { mutableStateOf(state.profile.phone) }
        var email by remember { mutableStateOf(state.profile.email) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Customer Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, singleLine = true)
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Mobile Number") }, singleLine = true)
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateCustomerProfile(name, phone, email)
                    showEditDialog = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showEditDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun CustomerDetailScreen(
    section: CustomerSection,
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    when (section) {
        CustomerSection.MY_RIDES -> OrdersHubScreen(state, viewModel, ServiceType.RIDE, showBackButton = true)
        CustomerSection.MY_FOOD_ORDERS -> OrdersHubScreen(state, viewModel, ServiceType.FOOD, showBackButton = true)
        CustomerSection.MY_GROCERY_ORDERS -> OrdersHubScreen(state, viewModel, ServiceType.GROCERY, showBackButton = true)
        CustomerSection.MY_PARCEL_BOOKINGS -> OrdersHubScreen(state, viewModel, ServiceType.PARCEL, showBackButton = true)
        CustomerSection.MY_TRANSPORT_BOOKINGS -> OrdersHubScreen(state, viewModel, ServiceType.TRANSPORT, showBackButton = true)
        CustomerSection.MY_PAYMENTS -> {
            BackHandler { viewModel.navigateBack() }
            Column(Modifier.fillMaxSize()) {
                GlassSubScreenHeader("My Payments", "UPI, Cards, Net Banking & Wallet", { viewModel.navigateBack() })
                WalletScreen(state, viewModel)
            }
        }
        else -> {
            BackHandler { viewModel.navigateBack() }
            CustomerUtilitySectionContent(section, state, viewModel)
        }
    }
}

@Composable
private fun CustomerUtilitySectionContent(
    section: CustomerSection,
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlassSubScreenHeader(
            title = section.title,
            subtitle = "SkyPulse Super App Account",
            onBack = { viewModel.navigateBack() }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (section) {
                CustomerSection.MY_COUPONS -> {
                    items(state.coupons) { c ->
                        Glass3DCard(cornerRadius = 20.dp) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    StatusPill(c.code, PremiumPink)
                                    Spacer(Modifier.height(4.dp))
                                    Text(c.title, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                    Text("${c.description} (${c.serviceScope})", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                }
                                GradientPillButton("Apply", {
                                    viewModel.applyCoupon(c.code)
                                    viewModel.navigateTo(AppRoute.MultiCart)
                                })
                            }
                        }
                    }
                }

                CustomerSection.MY_ADDRESSES -> {
                    items(state.addresses) { addr ->
                        Glass3DCard(cornerRadius = 20.dp, onClick = { viewModel.updateLocation(addr.fullAddress) }) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatusPill(addr.tag, SkyBlue)
                                    if (addr.isDefault) StatusPill("DEFAULT", EmeraldSuccess)
                                }
                                Text(addr.label, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                Text(addr.fullAddress, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            }
                        }
                    }
                    item {
                        var newLabel by remember { mutableStateOf("") }
                        var newFull by remember { mutableStateOf("") }
                        Glass3DCard(cornerRadius = 22.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Add New Saved Address", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                OutlinedTextField(value = newLabel, onValueChange = { newLabel = it }, label = { Text("Label (e.g., Gym, Studio)") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = newFull, onValueChange = { newFull = it }, label = { Text("Complete Address") }, modifier = Modifier.fillMaxWidth())
                                GradientPillButton("Save Address", {
                                    viewModel.addSavedAddress(newLabel, newFull, "Custom")
                                    newLabel = ""
                                    newFull = ""
                                }, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }

                CustomerSection.MY_FAVOURITES -> {
                    val favRests = state.restaurants.filter { it.isFavourite }
                    val favGroc = state.groceryProducts.filter { it.isWishlisted }
                    item { Text("Favourite Restaurants (${favRests.size})", style = MaterialTheme.typography.titleMedium, color = DarkNavy) }
                    items(favRests) { r ->
                        Glass3DCard(cornerRadius = 18.dp, onClick = { viewModel.navigateTo(AppRoute.Food) }) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(r.name, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                    Text("${r.cuisine} • ★ ${r.rating}", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                }
                                StatusPill("Order", PremiumPink)
                            }
                        }
                    }
                    item { Text("Wishlisted Groceries (${favGroc.size})", style = MaterialTheme.typography.titleMedium, color = DarkNavy) }
                    items(favGroc) { g ->
                        Glass3DCard(cornerRadius = 18.dp, onClick = { viewModel.navigateTo(AppRoute.Grocery) }) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(g.name, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                    Text("${g.unit} • ₹${g.price}", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                }
                                StatusPill("Shop", SkyBlue)
                            }
                        }
                    }
                }

                CustomerSection.MY_REVIEWS -> {
                    items(state.reviews) { rev ->
                        Glass3DCard(cornerRadius = 18.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    StatusPill(rev.serviceType.label, SkyBlue)
                                    Text("★".repeat(rev.rating), color = AmberWarning)
                                }
                                Text(rev.targetName, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                Text(rev.comment, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            }
                        }
                    }
                }

                CustomerSection.HELP_SUPPORT -> {
                    item {
                        Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 24.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("24/7 Gemini AI Concierge & Support", style = MaterialTheme.typography.titleLarge, color = PureWhite)
                                Text(
                                    "Get instant help with rides, food delays, grocery refunds, parcel OTPs, or transport invoices.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightSkyBlue
                                )
                                GradientPillButton(
                                    text = "Chat with SkyPulse AI Support",
                                    icon = Icons.Default.AutoAwesome,
                                    onClick = { viewModel.navigateTo(AppRoute.AiAssistant) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                CustomerSection.SETTINGS -> {
                    item {
                        var pushAlerts by remember { mutableStateOf(true) }
                        var liveActivities by remember { mutableStateOf(true) }
                        var safeRideShare by remember { mutableStateOf(true) }
                        Glass3DCard(cornerRadius = 22.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Push Order Notifications", style = MaterialTheme.typography.bodyMedium, color = DarkNavy)
                                    Switch(checked = pushAlerts, onCheckedChange = { pushAlerts = it })
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Home Live Activity Card", style = MaterialTheme.typography.bodyMedium, color = DarkNavy)
                                    Switch(checked = liveActivities, onCheckedChange = { liveActivities = it })
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Auto-share Ride Tracking with Family", style = MaterialTheme.typography.bodyMedium, color = DarkNavy)
                                    Switch(checked = safeRideShare, onCheckedChange = { safeRideShare = it })
                                }
                            }
                        }
                    }
                }

                CustomerSection.FAMILY_ACCOUNT -> {
                    item {
                        Glass3DCard(cornerRadius = 22.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Family Profiles & Shared Wallet", style = MaterialTheme.typography.titleLarge, color = DarkNavy)
                                Text("Book rides, groceries, or meals for family members with dedicated saved addresses.", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                GlassChip(
                                    text = "Personal (${state.profile.name})",
                                    selected = state.activeFamilyMemberId == null,
                                    onClick = { viewModel.selectFamilyProfile(null) }
                                )
                            }
                        }
                    }
                    items(state.familyMembers) { member ->
                        val active = state.activeFamilyMemberId == member.id
                        Glass3DCard(
                            cornerRadius = 20.dp,
                            onClick = { viewModel.selectFamilyProfile(member.id) }
                        ) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text("${member.name} (${member.relation})", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                    Text(member.savedAddress, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                                    Text("Monthly Limit: ₹${member.monthlyLimit}", style = MaterialTheme.typography.labelSmall, color = PremiumPink)
                                }
                                StatusPill(if (active) "ACTIVE" else "SWITCH", if (active) EmeraldSuccess else SkyBlue)
                            }
                        }
                    }
                    item {
                        var fName by remember { mutableStateOf("") }
                        var fRel by remember { mutableStateOf("Parent") }
                        var fAddr by remember { mutableStateOf("") }
                        Glass3DCard(cornerRadius = 22.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Add Family Member", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                OutlinedTextField(value = fName, onValueChange = { fName = it }, label = { Text("Member Name") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = fRel, onValueChange = { fRel = it }, label = { Text("Relation") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = fAddr, onValueChange = { fAddr = it }, label = { Text("Saved Address") }, modifier = Modifier.fillMaxWidth())
                                GradientPillButton("Add to Family Account", {
                                    viewModel.addFamilyMember(fName, fRel, "+91 98000 12345", fAddr, 3000)
                                    fName = ""
                                    fAddr = ""
                                }, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }

                CustomerSection.BUSINESS_DASHBOARD -> {
                    item {
                        val biz = state.businessProfile
                        Glass3DCard(gradientBrush = CardIndigoSkyGradient, cornerRadius = 24.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusPill("ENTERPRISE B2B ACCOUNT", PureWhite)
                                Text(biz.companyName, style = MaterialTheme.typography.headlineSmall, color = PureWhite)
                                Text("GSTIN: ${biz.gstNumber}", style = MaterialTheme.typography.bodySmall, color = PureWhite.copy(alpha = 0.9f))
                                Spacer(Modifier.height(6.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Credit Limit: ₹${biz.monthlyCreditLimit}", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                                    Text("Active Bulk Jobs: ${biz.activeBulkShipments}", style = MaterialTheme.typography.labelMedium, color = PalePink)
                                }
                            }
                        }
                    }
                    item {
                        Glass3DCard(cornerRadius = 22.dp) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Bulk Dispatch Quick Actions", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                GradientPillButton(
                                    text = "Book Bulk Parcel Dispatch",
                                    onClick = { viewModel.navigateTo(AppRoute.Parcel) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                GradientPillButton(
                                    text = "Book Commercial Fleet / Truck",
                                    onClick = { viewModel.navigateTo(AppRoute.Transport) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}
