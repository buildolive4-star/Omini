package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.data.DemoDataSeeder
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.InteractiveRouteMapCanvas
import com.example.ui.components.StatusPill
import com.example.ui.theme.CardCoralPinkGradient
import com.example.ui.theme.CardIndigoSkyGradient
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel
import kotlin.math.roundToInt

@Composable
fun ParcelDeliveryScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var pickup by remember { mutableStateOf(state.profile.currentLocation) }
    var drop by remember { mutableStateOf("HSR Layout Sector 2, Bengaluru") }
    var senderName by remember { mutableStateOf(state.profile.name) }
    var senderMobile by remember { mutableStateOf(state.profile.phone) }
    var receiverName by remember { mutableStateOf("Neha Kapoor") }
    var receiverMobile by remember { mutableStateOf("+91 98450 77889") }
    var selectedCategory by remember { mutableStateOf(DemoDataSeeder.parcelCategories.first()) }
    var weightKg by remember { mutableStateOf("1.5") }
    var dimensions by remember { mutableStateOf("25 x 18 x 12 cm") }
    var instructions by remember { mutableStateOf("Handle with care • Call at gate") }
    var isScheduled by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf("Express Bike") }

    val estimatedFare = remember(weightKg, selectedVehicle, isScheduled) {
        val w = weightKg.toDoubleOrNull() ?: 1.0
        val base = if (selectedVehicle == "Express Bike") 59 else 119
        val schedDiscount = if (isScheduled) -10 else 0
        (base + (w * 18).roundToInt() + schedDiscount).coerceAtLeast(49)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("parcel_screen")
    ) {
        GlassSubScreenHeader(
            title = "Send Parcel",
            subtitle = "Porter-style Doorstep Pickup • Dual OTP & Proof of Delivery",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Glass3DCard(gradientBrush = CardCoralPinkGradient, cornerRadius = 24.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            StatusPill("INSURED UP TO ₹10,000", PureWhite)
                            Spacer(Modifier.height(6.dp))
                            Text("Safe Hyperlocal Parcel", style = MaterialTheme.typography.headlineSmall, color = PureWhite)
                            Text("Pickup OTP + Delivery OTP + Digital POD", style = MaterialTheme.typography.bodySmall, color = PureWhite.copy(alpha = 0.9f))
                        }
                        Image(
                            painter = painterResource(id = R.drawable.img_service_parcel),
                            contentDescription = "Parcel 3D",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(82.dp)
                                .clip(RoundedCornerShape(18.dp))
                        )
                    }
                }
            }

            item {
                InteractiveRouteMapCanvas(
                    pickupLabel = pickup,
                    dropLabel = drop,
                    progress = 0.35f,
                    vehicleTag = selectedVehicle
                )
            }

            item {
                Glass3DCard(cornerRadius = 22.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Pickup & Drop Details", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        OutlinedTextField(
                            value = pickup,
                            onValueChange = { pickup = it },
                            label = { Text("Pickup Address") },
                            leadingIcon = { Icon(Icons.Default.MyLocation, null, tint = SkyBlue) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = drop,
                            onValueChange = { drop = it },
                            label = { Text("Drop Address") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = PremiumPink) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = senderName,
                                onValueChange = { senderName = it },
                                label = { Text("Sender Name") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = senderMobile,
                                onValueChange = { senderMobile = it },
                                label = { Text("Sender Mobile") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = receiverName,
                                onValueChange = { receiverName = it },
                                label = { Text("Receiver Name") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = receiverMobile,
                                onValueChange = { receiverMobile = it },
                                label = { Text("Receiver Mobile") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                Glass3DCard(cornerRadius = 22.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Parcel Category & Specs", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(DemoDataSeeder.parcelCategories) { cat ->
                                GlassChip(
                                    text = cat,
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat }
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightKg,
                                onValueChange = { weightKg = it },
                                label = { Text("Weight (kg)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = dimensions,
                                onValueChange = { dimensions = it },
                                label = { Text("Dimensions") },
                                singleLine = true,
                                modifier = Modifier.weight(1.4f)
                            )
                        }
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("Delivery Instructions") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GlassChip("Instant Pickup", !isScheduled, { isScheduled = false })
                            GlassChip("Schedule Later", isScheduled, { isScheduled = true })
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GlassChip("Express Bike", selectedVehicle == "Express Bike", { selectedVehicle = "Express Bike" })
                            GlassChip("Mini Van", selectedVehicle == "Mini Van", { selectedVehicle = "Mini Van" })
                        }
                    }
                }
            }

            item {
                GradientPillButton(
                    text = "BOOK PARCEL • ₹$estimatedFare",
                    icon = Icons.Default.Inventory2,
                    onClick = {
                        viewModel.bookParcel(
                            pickup = pickup,
                            drop = drop,
                            senderName = senderName,
                            receiverName = receiverName,
                            category = selectedCategory,
                            weightKg = weightKg,
                            isScheduled = isScheduled,
                            vehicleName = selectedVehicle,
                            estimatedFare = estimatedFare
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "confirm_parcel_button"
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GoodsTransportScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var pickup by remember { mutableStateOf(state.profile.currentLocation) }
    var drop by remember { mutableStateOf("Whitefield ITPL Main Rd, Bengaluru") }
    var selectedVehicle by remember { mutableStateOf(DemoDataSeeder.transportVehicles[2]) } // Tata Ace default
    var goodsType by remember { mutableStateOf("Home Furniture & Appliances") }
    var weightKg by remember { mutableStateOf("350") }
    var dimensions by remember { mutableStateOf("6ft x 4ft cargo") }
    var loadingAssist by remember { mutableStateOf(true) }
    var unloadingAssist by remember { mutableStateOf(true) }
    var helpersCount by remember { mutableIntStateOf(2) }
    var scheduleOption by remember { mutableStateOf("Pickup in 15 mins") }

    val fareBreakdown = remember(selectedVehicle, loadingAssist, unloadingAssist, helpersCount, state.adminConfig.surgeMultiplier) {
        val base = ((selectedVehicle.baseFare + 10 * selectedVehicle.perKmRate) * state.adminConfig.surgeMultiplier).roundToInt()
        val assist = (if (loadingAssist) 120 else 0) + (if (unloadingAssist) 120 else 0)
        val helpers = helpersCount * 150
        Triple(base, assist + helpers, base + assist + helpers)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("transport_screen")
    ) {
        GlassSubScreenHeader(
            title = "Goods Transport",
            subtitle = "Mini Trucks • Pickup • Loading Assistance",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Glass3DCard(gradientBrush = CardIndigoSkyGradient, cornerRadius = 24.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            StatusPill("VERIFIED FLEET • GPS", PureWhite)
                            Spacer(Modifier.height(6.dp))
                            Text("Move Anything, Effortlessly", style = MaterialTheme.typography.headlineSmall, color = PureWhite)
                            Text("Two Wheeler to 19ft Large Truck with Helpers", style = MaterialTheme.typography.bodySmall, color = PureWhite.copy(alpha = 0.9f))
                        }
                        Image(
                            painter = painterResource(id = R.drawable.img_service_transport),
                            contentDescription = "Transport 3D",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(82.dp)
                                .clip(RoundedCornerShape(18.dp))
                        )
                    }
                }
            }

            item {
                Glass3DCard(cornerRadius = 22.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Route & Cargo Info", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        OutlinedTextField(
                            value = pickup,
                            onValueChange = { pickup = it },
                            label = { Text("Pickup Location") },
                            leadingIcon = { Icon(Icons.Default.MyLocation, null, tint = SkyBlue) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = drop,
                            onValueChange = { drop = it },
                            label = { Text("Destination") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = PremiumPink) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = goodsType,
                            onValueChange = { goodsType = it },
                            label = { Text("Goods Type (Furniture, Electronics, Retail B2B…)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightKg,
                                onValueChange = { weightKg = it },
                                label = { Text("Weight (kg)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = dimensions,
                                onValueChange = { dimensions = it },
                                label = { Text("Cargo Dimensions") },
                                singleLine = true,
                                modifier = Modifier.weight(1.3f)
                            )
                        }
                    }
                }
            }

            item {
                Text("Select Commercial Vehicle (6 Fleet Options)", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            }

            items(DemoDataSeeder.transportVehicles, key = { it.id }) { vehicle ->
                val isSelected = selectedVehicle.id == vehicle.id
                val est = (vehicle.baseFare + 10 * vehicle.perKmRate)
                Glass3DCard(
                    cornerRadius = 20.dp,
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else null,
                    onClick = { selectedVehicle = vehicle },
                    modifier = Modifier.testTag("transport_vehicle_${vehicle.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) DarkNavy else PaleSky),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocalShipping, null, tint = if (isSelected) SkyBlue else DarkNavy)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(vehicle.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                StatusPill(vehicle.badge, if (isSelected) PremiumPink else SkyBlue)
                            }
                            Text(vehicle.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("₹$est", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            // Loading / Unloading & Helpers Assistance
            item {
                Glass3DCard(cornerRadius = 22.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Loading Assistance & Scheduling", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Loading Assistance (+₹120)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Switch(checked = loadingAssist, onCheckedChange = { loadingAssist = it })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Unloading Assistance (+₹120)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Switch(checked = unloadingAssist, onCheckedChange = { unloadingAssist = it })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Number of Helpers (₹150 each)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { helpersCount = (helpersCount - 1).coerceAtLeast(0) }) {
                                    Icon(Icons.Default.Remove, "Decrease helpers")
                                }
                                Text("$helpersCount", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                IconButton(onClick = { helpersCount = (helpersCount + 1).coerceAtMost(4) }) {
                                    Icon(Icons.Default.Add, "Increase helpers")
                                }
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf("Pickup in 15 mins", "Today 4:00 PM", "Tomorrow 9:00 AM")) { slot ->
                                GlassChip(slot, scheduleOption == slot, { scheduleOption = slot })
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Vehicle Fare: ₹${fareBreakdown.first}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Helpers & Load: ₹${fareBreakdown.second}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            StatusPill("Total ₹${fareBreakdown.third}", EmeraldSuccess)
                        }
                    }
                }
            }

            item {
                GradientPillButton(
                    text = "CONFIRM ${selectedVehicle.name.uppercase()} • ₹${fareBreakdown.third}",
                    icon = Icons.Default.LocalShipping,
                    onClick = {
                        viewModel.bookTransport(
                            pickup = pickup,
                            drop = drop,
                            vehicle = selectedVehicle,
                            goodsType = goodsType,
                            weightKg = weightKg,
                            loadingAssist = loadingAssist,
                            unloadingAssist = unloadingAssist,
                            helpersCount = helpersCount,
                            scheduleText = scheduleOption,
                            totalFare = fareBreakdown.third
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "confirm_transport_button"
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
