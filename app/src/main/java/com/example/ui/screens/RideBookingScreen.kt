package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.ActiveRide
import com.example.data.DemoDataSeeder
import com.example.data.RidePhase
import com.example.data.RideVehicleOption
import com.example.ui.components.AnimatedOrderStepper
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.InteractiveRouteMapCanvas
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
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
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SkyToPinkBrush
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel
import kotlin.math.roundToInt

@Composable
fun RideBookingScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var pickup by remember { mutableStateOf(state.profile.currentLocation) }
    var drop by remember { mutableStateOf("MG Road Metro Station, Bengaluru") }
    var showDropSuggestions by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf(DemoDataSeeder.rideVehicles.first()) }
    var paymentMethod by remember { mutableStateOf(state.selectedPaymentMethod) }
    var showChatDialog by remember { mutableStateOf(false) }
    var showCallDialog by remember { mutableStateOf(false) }

    val distanceKm = remember(pickup, drop) {
        ((pickup.length + drop.length) % 9 + 3.4).let { (it * 10).roundToInt() / 10.0 }
    }
    val surge = state.adminConfig.surgeMultiplier

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ride_booking_screen")
    ) {
        GlassSubScreenHeader(
            title = "SkyPulse Ride",
            subtitle = "Bike • Auto • Cab • Instant Captain Matching",
            onBack = { viewModel.navigateBack() },
            trailing = {
                if (surge > 1.0) {
                    StatusPill("${surge}x Surge", AmberWarning)
                }
            }
        )

        val activeRide = state.activeRide
        if (activeRide != null) {
            ActiveRideTrackingView(
                ride = activeRide,
                onVerifyOtp = viewModel::verifyRideOtpAndStartTrip,
                onOpenChat = { showChatDialog = true },
                onOpenCall = { showCallDialog = true },
                onCancelRide = viewModel::cancelActiveRide,
                onSubmitRating = viewModel::rateAndCloseRide
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    InteractiveRouteMapCanvas(
                        pickupLabel = pickup,
                        dropLabel = drop,
                        progress = 0.18f,
                        vehicleTag = selectedVehicle.name
                    )
                }

                // Pickup & Destination Search with Autocomplete
                item {
                    Glass3DCard(cornerRadius = 24.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = pickup,
                                onValueChange = { pickup = it },
                                label = { Text("Pickup Location") },
                                leadingIcon = { Icon(Icons.Default.MyLocation, null, tint = SkyBlue) },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("ride_pickup_input")
                            )

                            OutlinedTextField(
                                value = drop,
                                onValueChange = {
                                    drop = it
                                    showDropSuggestions = true
                                },
                                label = { Text("Where to? (Destination)") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = PremiumPink) },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("ride_drop_input")
                            )

                            AnimatedVisibility(visible = showDropSuggestions) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val matches = DemoDataSeeder.popularLocations.filter {
                                        drop.isBlank() || it.contains(drop, ignoreCase = true)
                                    }.take(4)
                                    matches.forEach { suggestion ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    drop = suggestion
                                                    showDropSuggestions = false
                                                }
                                        ) {
                                            Text(
                                                text = suggestion,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatusPill("Est. Distance: $distanceKm km", SkyBlue)
                                StatusPill("ETA: ${(distanceKm * 2.5).roundToInt()} mins", PremiumPink)
                            }
                        }
                    }
                }

                // Vehicle Selection Cards
                item {
                    Text("Choose Your Ride", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                }

                items(DemoDataSeeder.rideVehicles, key = { it.id }) { vehicle ->
                    val fare = ((vehicle.baseFare + distanceKm * vehicle.perKmRate) * surge).roundToInt()
                    val isSelected = selectedVehicle.id == vehicle.id
                    RideVehicleCard(
                        vehicle = vehicle,
                        estimatedFare = fare,
                        selected = isSelected,
                        onClick = { selectedVehicle = vehicle }
                    )
                }

                // Payment Selection
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Payment Mode", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf("SkyPulse Wallet", "UPI", "Card •••• 4092", "Cash")) { method ->
                                GlassChip(
                                    text = method,
                                    selected = paymentMethod == method,
                                    onClick = { paymentMethod = method }
                                )
                            }
                        }
                    }
                }

                // Confirm Booking CTA
                item {
                    val finalFare = ((selectedVehicle.baseFare + distanceKm * selectedVehicle.perKmRate) * surge).roundToInt()
                    GradientPillButton(
                        text = "CONFIRM ${selectedVehicle.name.uppercase()} • ₹$finalFare",
                        onClick = {
                            if (drop.isBlank()) {
                                viewModel.showMessage("Please enter a destination first.")
                            } else {
                                viewModel.bookRide(pickup, drop, selectedVehicle, paymentMethod)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTagName = "confirm_ride_button"
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }

    // In-App Driver Chat Modal
    if (showChatDialog && state.activeRide != null) {
        var chatDraft by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showChatDialog = false },
            title = { Text("Chat with ${state.activeRide.driver.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.activeRide.chatMessages.forEach { msg ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (msg.isFromUser) PalePink else PaleSky,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                Text(msg.sender, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(msg.text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = chatDraft,
                            onValueChange = { chatDraft = it },
                            placeholder = { Text("Message captain…") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            viewModel.sendDriverChatMessage(chatDraft)
                            chatDraft = ""
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = PremiumPink)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showChatDialog = false }) { Text("Close") } }
        )
    }

    // Call Driver Demo Dialog
    if (showCallDialog && state.activeRide != null) {
        AlertDialog(
            onDismissRequest = { showCallDialog = false },
            title = { Text("Calling Captain ${state.activeRide.driver.name}") },
            text = {
                Text("Masked privacy call to ${state.activeRide.driver.phone} (Prototype demo call simulation).")
            },
            confirmButton = { TextButton(onClick = { showCallDialog = false }) { Text("End Call") } }
        )
    }
}

@Composable
private fun RideVehicleCard(
    vehicle: RideVehicleOption,
    estimatedFare: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (vehicle.category) {
        "Bike" -> Icons.Default.DirectionsBike
        "Auto" -> Icons.Default.ElectricRickshaw
        else -> Icons.Default.DirectionsCar
    }
    Glass3DCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vehicle_${vehicle.id}"),
        cornerRadius = 22.dp,
        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else null,
        elevation = if (selected) 12.dp else 5.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selected) SkyToPinkBrush else androidx.compose.ui.graphics.Brush.linearGradient(listOf(PaleSky, PalePink))),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = if (selected) PureWhite else DarkNavy, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(vehicle.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    StatusPill(vehicle.badge, if (selected) PremiumPink else SkyBlue)
                }
                Text(vehicle.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${vehicle.etaMinutes} mins away • ${vehicle.seats} seat(s)", style = MaterialTheme.typography.labelSmall, color = SkyBlue)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("₹$estimatedFare", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
                Text("Est. fare", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ActiveRideTrackingView(
    ride: ActiveRide,
    onVerifyOtp: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenCall: () -> Unit,
    onCancelRide: () -> Unit,
    onSubmitRating: (Int, String) -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var feedback by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            InteractiveRouteMapCanvas(
                pickupLabel = ride.pickup,
                dropLabel = ride.drop,
                progress = ride.progress,
                vehicleTag = "${ride.vehicle.name} • ${ride.phase.label}"
            )
        }

        item {
            Glass3DCard(cornerRadius = 24.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(ride.phase.label, EmeraldSuccess)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkNavy
                        ) {
                            Text(
                                text = "OTP: ${ride.otp}",
                                style = MaterialTheme.typography.titleMedium,
                                color = PureWhite,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    AnimatedOrderStepper(
                        steps = listOf("Matched", "Arriving", "On Trip", "Done"),
                        currentStep = ride.phase.ordinal
                    )

                    // Driver profile card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = BorderStroke(1.dp, LightSkyBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_service_ride),
                                contentDescription = "Vehicle preview",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(ride.driver.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(Modifier.width(4.dp))
                                    Icon(Icons.Default.VerifiedUser, null, tint = SkyBlue, modifier = Modifier.size(16.dp))
                                }
                                Text("${ride.driver.vehicleModel} • ${ride.driver.vehiclePlate}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("★ ${ride.driver.rating} (${ride.driver.tripsCount} trips) • ₹${ride.fare} (${ride.paymentMethod})", style = MaterialTheme.typography.labelSmall, color = PremiumPink)
                            }
                        }
                    }

                    // Call, Chat, Cancel row
                    if (ride.phase != RidePhase.COMPLETED) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onOpenCall,
                                modifier = Modifier.weight(1f).testTag("ride_call_button")
                            ) {
                                Icon(Icons.Default.Call, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Call")
                            }
                            OutlinedButton(
                                onClick = onOpenChat,
                                modifier = Modifier.weight(1f).testTag("ride_chat_button")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Chat")
                            }
                            OutlinedButton(
                                onClick = onCancelRide,
                                modifier = Modifier.weight(1f).testTag("ride_cancel_button")
                            ) {
                                Icon(Icons.Default.Cancel, null, tint = RoseError, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Cancel", color = RoseError)
                            }
                        }
                    }

                    if (ride.phase == RidePhase.SEARCHING || ride.phase == RidePhase.DRIVER_ASSIGNED) {
                        GradientPillButton(
                            text = "Verify OTP (${ride.otp}) & Start Live Trip",
                            onClick = onVerifyOtp,
                            modifier = Modifier.fillMaxWidth(),
                            testTagName = "verify_otp_start_trip"
                        )
                    }

                    if (ride.phase == RidePhase.COMPLETED) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("You've arrived! Rate your Captain", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (1..5).forEach { star ->
                                    IconButton(onClick = { selectedStars = star }) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "$star stars",
                                            tint = if (star <= selectedStars) AmberWarning else LightPink,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = feedback,
                                onValueChange = { feedback = it },
                                label = { Text("Share feedback (optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                            GradientPillButton(
                                text = "Submit Rating & Complete",
                                onClick = { onSubmitRating(selectedStars, feedback) },
                                modifier = Modifier.fillMaxWidth(),
                                testTagName = "submit_ride_rating"
                            )
                        }
                    }
                }
            }
        }
    }
}
