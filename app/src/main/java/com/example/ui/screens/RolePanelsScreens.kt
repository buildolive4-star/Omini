package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.data.AppRoute
import com.example.data.PartnerCategory
import com.example.data.PartnerJobStatus
import com.example.data.UserRole
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardMintSkyGradient
import com.example.ui.theme.CardPinkGradient
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
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

@Composable
fun RoleAuthGateScreen(
    targetRole: UserRole,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var emailOrId by remember(targetRole) { mutableStateOf("${targetRole.name.lowercase()}@skypulse.demo") }
    var pinInput by remember(targetRole) { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("role_auth_gate")
    ) {
        GlassSubScreenHeader(
            title = "${targetRole.title} Access",
            subtitle = "Role-based authorization required",
            onBack = { viewModel.navigateBack() }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Glass3DCard(cornerRadius = 28.dp) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Lock, null, tint = PremiumPink, modifier = Modifier.size(44.dp))
                    Text("Authorized ${targetRole.title} Login", style = MaterialTheme.typography.headlineSmall, color = DarkNavy)
                    Text(
                        "Sensitive operational controls are protected from standard customer accounts. Enter your authorized ID & demo access PIN.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedNavy
                    )
                    OutlinedTextField(
                        value = emailOrId,
                        onValueChange = { emailOrId = it },
                        label = { Text("Authorized ID / Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it.filter(Char::isDigit).take(4) },
                        label = { Text("4-Digit Access PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("role_pin_input")
                    )
                    TextButton(
                        onClick = { pinInput = targetRole.demoCode },
                        modifier = Modifier.testTag("autofill_role_pin")
                    ) {
                        Text("Prototype Helper: Fill Demo PIN (${targetRole.demoCode})", color = SkyBlue)
                    }
                    GradientPillButton(
                        text = "VERIFY & OPEN PANEL",
                        onClick = { viewModel.authenticateRole(targetRole, pinInput, emailOrId) },
                        modifier = Modifier.fillMaxWidth(),
                        testTagName = "verify_role_button"
                    )
                }
            }
        }
    }
}

@Composable
fun PartnerDashboardScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    val partner = state.partnerProfile
    // Partner only receives jobs compatible with their verified category
    val compatibleJobs = state.partnerJobs.filter { it.category == partner.category }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("partner_dashboard_screen")
    ) {
        GlassSubScreenHeader(
            title = "Partner Dashboard",
            subtitle = "${partner.name} • ${partner.category.label} • ★ ${partner.rating}",
            onBack = { viewModel.navigateBack() },
            trailing = {
                StatusPill(partner.verificationStatus, if (partner.verificationStatus == "Verified") EmeraldSuccess else AmberWarning)
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Online / Offline & Earnings Header
            item {
                Glass3DCard(gradientBrush = NavyGlowBrush, cornerRadius = 26.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Duty Status", style = MaterialTheme.typography.labelSmall, color = LightSkyBlue)
                                Text(
                                    text = if (partner.isOnline) "ONLINE • Receiving Jobs" else "OFFLINE • Paused",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PureWhite
                                )
                            }
                            Switch(
                                checked = partner.isOnline,
                                onCheckedChange = { viewModel.togglePartnerOnline() },
                                modifier = Modifier.testTag("partner_online_toggle")
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            PartnerMetric("Today's Earnings", "₹${partner.todayEarnings}")
                            PartnerMetric("Completed", "${partner.completedToday}")
                            PartnerMetric("Incentive Bonus", "+₹${partner.incentiveBonus}")
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "Daily Incentive Progress: ${partner.completedToday}/${partner.incentiveTarget} trips",
                                style = MaterialTheme.typography.labelSmall,
                                color = LightPink
                            )
                            LinearProgressIndicator(
                                progress = { (partner.completedToday.toFloat() / partner.incentiveTarget).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(CircleShape),
                                color = SkyBlue,
                                trackColor = PureWhite.copy(alpha = 0.2f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GradientPillButton(
                                text = "Request Payout",
                                onClick = { viewModel.showMessage("Payout of ₹${partner.todayEarnings} initiated to ${partner.bankAccount} (Demo).") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = { viewModel.navigateTo(AppRoute.PartnerRegistration) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("KYC Docs", color = PureWhite)
                            }
                        }
                    }
                }
            }

            // Partner Category Switcher (filters jobs strictly to matching verified category)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Verified Vehicle & Service Category", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                    Text(
                        "Safety Rule: You only receive requests matching your active verified vehicle category.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedNavy
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(PartnerCategory.entries) { cat ->
                            GlassChip(
                                text = cat.label,
                                selected = partner.category == cat,
                                onClick = { viewModel.selectPartnerCategory(cat) }
                            )
                        }
                    }
                }
            }

            item {
                Text("Compatible Requests (${compatibleJobs.size})", style = MaterialTheme.typography.titleLarge, color = DarkNavy)
            }

            if (!partner.isOnline) {
                item {
                    Glass3DCard(cornerRadius = 20.dp) {
                        Text("Go ONLINE above to receive incoming ${partner.category.label} bookings.", style = MaterialTheme.typography.bodyMedium, color = MutedNavy)
                    }
                }
            } else if (compatibleJobs.isEmpty()) {
                item {
                    Glass3DCard(cornerRadius = 20.dp) {
                        Text("No pending ${partner.category.label} requests right now. Book one from the Customer side or switch category to test!", style = MaterialTheme.typography.bodyMedium, color = MutedNavy)
                    }
                }
            } else {
                items(compatibleJobs, key = { it.id }) { job ->
                    Glass3DCard(cornerRadius = 22.dp, modifier = Modifier.testTag("partner_job_${job.id}")) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                StatusPill(job.status.label, if (job.status == PartnerJobStatus.COMPLETED) EmeraldSuccess else PremiumPink)
                                Text("₹${job.payout} payout", style = MaterialTheme.typography.titleLarge, color = DarkNavy, fontWeight = FontWeight.ExtraBold)
                            }
                            Text("${job.itemSummary} • ${job.distanceKm} km", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                            Text("Customer: ${job.customerName} (${job.customerPhone})", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            Text("Pickup: ${job.pickup}", style = MaterialTheme.typography.bodySmall, color = SkyBlue)
                            Text("Drop: ${job.drop}", style = MaterialTheme.typography.bodySmall, color = PremiumPink)
                            Text("Pickup OTP: ${job.pickupOtp} • Delivery OTP: ${job.deliveryOtp}", style = MaterialTheme.typography.labelSmall, color = DarkNavy)

                            when (job.status) {
                                PartnerJobStatus.PENDING -> {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        GradientPillButton(
                                            text = "Accept Job",
                                            onClick = { viewModel.acceptPartnerJob(job.id) },
                                            modifier = Modifier.weight(1f),
                                            testTagName = "accept_job_${job.id}"
                                        )
                                        OutlinedButton(
                                            onClick = { viewModel.rejectPartnerJob(job.id) },
                                            modifier = Modifier.weight(0.7f)
                                        ) {
                                            Text("Reject", color = RoseError)
                                        }
                                    }
                                }
                                PartnerJobStatus.ACCEPTED -> {
                                    GradientPillButton(
                                        text = "Verify Pickup OTP (${job.pickupOtp}) & Start",
                                        onClick = { viewModel.advancePartnerJob(job.id) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                PartnerJobStatus.PICKED_UP -> {
                                    GradientPillButton(
                                        text = "Verify Drop OTP (${job.deliveryOtp}) & Complete",
                                        onClick = { viewModel.advancePartnerJob(job.id) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                PartnerJobStatus.COMPLETED -> {
                                    StatusPill("Settled +₹${job.payout}", EmeraldSuccess)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PartnerMetric(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = LightSkyBlue)
        Text(value, style = MaterialTheme.typography.titleLarge, color = PureWhite, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun PartnerRegistrationScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    val p = state.partnerProfile
    var name by remember { mutableStateOf(p.name) }
    var mobile by remember { mutableStateOf(p.mobile) }
    var category by remember { mutableStateOf(p.category) }
    var aadhaar by remember { mutableStateOf(p.aadhaarNumber) }
    var pan by remember { mutableStateOf(p.panNumber) }
    var dl by remember { mutableStateOf(p.drivingLicence) }
    var rc by remember { mutableStateOf(p.vehicleRc) }
    var insurance by remember { mutableStateOf(p.vehicleInsurance) }
    var bank by remember { mutableStateOf(p.bankAccount) }
    var docsUploaded by remember { mutableStateOf(p.documentsUploaded) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("partner_registration_screen")
    ) {
        GlassSubScreenHeader(
            title = "Become a Delivery Partner",
            subtitle = "KYC, Vehicle RC, Insurance & Bank Onboarding",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Glass3DCard(cornerRadius = 22.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Verification Status", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                            StatusPill(p.verificationStatus, if (p.verificationStatus == "Verified") EmeraldSuccess else AmberWarning)
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(PartnerCategory.entries) { cat ->
                                GlassChip(cat.label, category == cat, { category = cat })
                            }
                        }
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = aadhaar, onValueChange = { aadhaar = it }, label = { Text("Aadhaar Number") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = pan, onValueChange = { pan = it }, label = { Text("PAN Number") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = dl, onValueChange = { dl = it }, label = { Text("Driving Licence Number") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = rc, onValueChange = { rc = it }, label = { Text("Vehicle RC Number") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = insurance, onValueChange = { insurance = it }, label = { Text("Vehicle Insurance Policy") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = bank, onValueChange = { bank = it }, label = { Text("Bank Account / UPI for Payouts") }, modifier = Modifier.fillMaxWidth())

                        OutlinedButton(
                            onClick = {
                                docsUploaded = true
                                viewModel.showMessage("KYC document scans attached (Demo upload).")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(if (docsUploaded) Icons.Default.Verified else Icons.Default.UploadFile, null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (docsUploaded) "KYC & Vehicle Docs Uploaded ✓" else "Upload KYC & RC Scans")
                        }

                        GradientPillButton(
                            text = "SUBMIT PARTNER APPLICATION",
                            onClick = {
                                viewModel.submitPartnerKyc(name, mobile, category, aadhaar, pan, dl, rc, insurance, bank)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            testTagName = "submit_partner_kyc_button"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantOwnerPanelScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var activeTab by remember { mutableStateOf("Orders") }
    var newDishName by remember { mutableStateOf("") }
    var newDishPrice by remember { mutableStateOf("") }
    var newDishVeg by remember { mutableStateOf(true) }

    val ownMenu = state.foodMenuItems.filter { it.restaurantId == "rest_1" }
    val grossSales = state.restaurantTickets.filter { it.status != "Rejected" }.sumOf { it.totalAmount } + 18420
    val commission = (grossSales * state.adminConfig.commissionPercent / 100.0).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("restaurant_panel_screen")
    ) {
        GlassSubScreenHeader(
            title = "Restaurant Owner Panel",
            subtitle = "OvenCraft Woodfire Pizza • ★ 4.7",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Glass3DCard(gradientBrush = CardPinkGradient, cornerRadius = 24.dp) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Today's Revenue", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                            Text("₹$grossSales", style = MaterialTheme.typography.headlineMedium, color = PureWhite, fontWeight = FontWeight.ExtraBold)
                        }
                        Column {
                            Text("Commission (${state.adminConfig.commissionPercent}%)", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                            Text("₹$commission", style = MaterialTheme.typography.titleLarge, color = PureWhite)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Net Payout", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                            Text("₹${grossSales - commission}", style = MaterialTheme.typography.titleLarge, color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GlassChip("Incoming Orders (${state.restaurantTickets.size})", activeTab == "Orders", { activeTab = "Orders" })
                    GlassChip("Menu Manager (${ownMenu.size})", activeTab == "Menu", { activeTab = "Menu" })
                }
            }

            if (activeTab == "Orders") {
                items(state.restaurantTickets, key = { it.id }) { ticket ->
                    Glass3DCard(cornerRadius = 20.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${ticket.id} • ${ticket.customerName}", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                StatusPill(ticket.status, if (ticket.status == "Rejected") RoseError else EmeraldSuccess)
                            }
                            Text(ticket.itemsSummary, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            Text("₹${ticket.totalAmount} • Prep: ${ticket.prepMinutes} mins • ${ticket.timestamp}", style = MaterialTheme.typography.labelSmall, color = PremiumPink)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (ticket.status == "Incoming") {
                                    GradientPillButton("Accept (20m)", { viewModel.updateRestaurantTicketStatus(ticket.id, "Preparing", 20) }, modifier = Modifier.weight(1f))
                                    OutlinedButton(onClick = { viewModel.updateRestaurantTicketStatus(ticket.id, "Rejected") }) { Text("Reject", color = RoseError) }
                                } else if (ticket.status == "Preparing") {
                                    GradientPillButton("Mark Ready for Pickup", { viewModel.updateRestaurantTicketStatus(ticket.id, "Ready") }, modifier = Modifier.fillMaxWidth())
                                } else if (ticket.status == "Ready") {
                                    GradientPillButton("Hand Over to Driver", { viewModel.updateRestaurantTicketStatus(ticket.id, "Dispatched") }, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Glass3DCard(cornerRadius = 20.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Add New Dish to Live Menu", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                            OutlinedTextField(value = newDishName, onValueChange = { newDishName = it }, label = { Text("Dish Name") }, modifier = Modifier.fillMaxWidth())
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = newDishPrice,
                                    onValueChange = { newDishPrice = it.filter(Char::isDigit) },
                                    label = { Text("Price (₹)") },
                                    modifier = Modifier.weight(1f)
                                )
                                GlassChip(if (newDishVeg) "Veg ✓" else "Non-Veg", newDishVeg, { newDishVeg = !newDishVeg })
                            }
                            GradientPillButton("Publish Dish", {
                                viewModel.addRestaurantMenuItem(newDishName, "Chef special house recipe", "Pizza", newDishPrice.toIntOrNull() ?: 249, newDishVeg)
                                newDishName = ""
                                newDishPrice = ""
                            }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                items(ownMenu, key = { it.id }) { dish ->
                    Glass3DCard(cornerRadius = 18.dp) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(dish.name, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                Text("₹${dish.price} • ${if (dish.isVeg) "Veg" else "Non-Veg"}", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            }
                            Switch(checked = dish.isAvailable, onCheckedChange = { viewModel.toggleMenuItemAvailability(dish.id) })
                            IconButton(onClick = { viewModel.deleteMenuItem(dish.id) }) {
                                Icon(Icons.Default.Delete, "Delete item", tint = RoseError)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryOwnerPanelScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var activeTab by remember { mutableStateOf("Orders") }
    var newProdName by remember { mutableStateOf("") }
    var newProdPrice by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("shop_panel_screen")
    ) {
        GlassSubScreenHeader(
            title = "Grocery Store Owner Panel",
            subtitle = "FreshMart Express • Inventory & Settlements",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Glass3DCard(gradientBrush = CardMintSkyGradient, cornerRadius = 24.dp) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Store Sales", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                            Text("₹24,680", style = MaterialTheme.typography.headlineMedium, color = PureWhite, fontWeight = FontWeight.ExtraBold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Next Settlement", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                            Text("₹21,470 (Tonight)", style = MaterialTheme.typography.titleMedium, color = PureWhite)
                        }
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GlassChip("Incoming Orders (${state.shopTickets.size})", activeTab == "Orders", { activeTab = "Orders" })
                    GlassChip("Stock & Inventory (${state.groceryProducts.size})", activeTab == "Inventory", { activeTab = "Inventory" })
                }
            }

            if (activeTab == "Orders") {
                items(state.shopTickets, key = { it.id }) { ticket ->
                    Glass3DCard(cornerRadius = 20.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${ticket.id} • ${ticket.customerName}", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                                StatusPill(ticket.status, EmeraldSuccess)
                            }
                            Text(ticket.itemsSummary, style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            Text("₹${ticket.totalAmount} • Rider: ${ticket.assignedDriver}", style = MaterialTheme.typography.labelSmall, color = SkyBlue)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (ticket.status == "Incoming") {
                                    GradientPillButton("Accept & Pack", { viewModel.updateShopTicketStatus(ticket.id, "Packing") }, modifier = Modifier.weight(1f))
                                    OutlinedButton(onClick = { viewModel.updateShopTicketStatus(ticket.id, "Rejected") }) { Text("Reject", color = RoseError) }
                                } else if (ticket.status == "Packing") {
                                    GradientPillButton("Assign Rider & Dispatch", { viewModel.updateShopTicketStatus(ticket.id, "Dispatched", "Kiran B.") }, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Glass3DCard(cornerRadius = 20.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Add Grocery Product", style = MaterialTheme.typography.titleMedium, color = DarkNavy)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(value = newProdName, onValueChange = { newProdName = it }, label = { Text("Product Name") }, modifier = Modifier.weight(1.4f))
                                OutlinedTextField(value = newProdPrice, onValueChange = { newProdPrice = it.filter(Char::isDigit) }, label = { Text("Price ₹") }, modifier = Modifier.weight(0.8f))
                            }
                            GradientPillButton("Add to Live Stock", {
                                viewModel.addGroceryProduct(newProdName, "Fruits", "500 g", newProdPrice.toIntOrNull() ?: 99, 25)
                                newProdName = ""
                                newProdPrice = ""
                            }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                items(state.groceryProducts, key = { it.id }) { prod ->
                    Glass3DCard(cornerRadius = 18.dp) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(prod.name, style = MaterialTheme.typography.titleSmall, color = DarkNavy)
                                Text("${prod.category} • ₹${prod.price} • Stock: ${prod.stock}", style = MaterialTheme.typography.bodySmall, color = MutedNavy)
                            }
                            IconButton(onClick = { viewModel.adjustGroceryStock(prod.id, -5) }) {
                                Icon(Icons.Default.Remove, "Decrease stock")
                            }
                            IconButton(onClick = { viewModel.adjustGroceryStock(prod.id, 5) }) {
                                Icon(Icons.Default.Add, "Increase stock", tint = SkyBlue)
                            }
                        }
                    }
                }
            }
        }
    }
}
