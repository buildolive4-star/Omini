package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AppRoute
import com.example.data.DemoDataSeeder
import com.example.data.FoodMenuItem
import com.example.data.Restaurant
import com.example.data.ServiceType
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardPinkGradient
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PalePink
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

@Composable
fun FoodDeliveryScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    var selectedRestaurantId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var vegOnly by remember { mutableStateOf(false) }
    var customizingItem by remember { mutableStateOf<FoodMenuItem?>(null) }

    BackHandler {
        if (selectedRestaurantId != null) selectedRestaurantId = null
        else viewModel.navigateBack()
    }

    val selectedRestaurant = state.restaurants.find { it.id == selectedRestaurantId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("food_delivery_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlassSubScreenHeader(
                title = selectedRestaurant?.name ?: "Food Delivery",
                subtitle = selectedRestaurant?.let { "${it.cuisine} • ★ ${it.rating} • ${it.deliveryMinutes} mins" }
                    ?: "Swiggy + Zomato experience • Multi-Service Cart ready",
                onBack = {
                    if (selectedRestaurantId != null) selectedRestaurantId = null
                    else viewModel.navigateBack()
                },
                trailing = {
                    Surface(
                        shape = CircleShape,
                        color = DarkNavy,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { viewModel.navigateTo(AppRoute.MultiCart) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ShoppingCart, "Cart", tint = PureWhite, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("${state.totalCartItemsCount}", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                        }
                    }
                }
            )

            if (selectedRestaurant != null) {
                val menu = state.foodMenuItems.filter { item ->
                    item.restaurantId == selectedRestaurant.id && item.isAvailable && (!vegOnly || item.isVeg)
                }
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Glass3DCard(gradientBrush = CardPinkGradient, cornerRadius = 24.dp) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    StatusPill(selectedRestaurant.offerTag, PureWhite)
                                    Spacer(Modifier.height(6.dp))
                                    Text(selectedRestaurant.name, style = MaterialTheme.typography.headlineSmall, color = PureWhite)
                                    Text(
                                        "Min order ₹${selectedRestaurant.minOrder} • ${selectedRestaurant.distanceKm} km away",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PureWhite.copy(alpha = 0.9f)
                                    )
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.img_service_food),
                                    contentDescription = selectedRestaurant.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(86.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Menu (${menu.size} items)", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                            GlassChip(
                                text = if (vegOnly) "Veg Only ✓" else "All Dishes",
                                selected = vegOnly,
                                onClick = { vegOnly = !vegOnly }
                            )
                        }
                    }

                    items(menu, key = { it.id }) { dish ->
                        val cartQty = state.cartItems.filter { it.itemId == dish.id }.sumOf { it.quantity }
                        FoodMenuItemCard(
                            item = dish,
                            quantityInCart = cartQty,
                            onAddClick = { customizingItem = dish },
                            onIncrement = {
                                viewModel.addToCart(dish.id, dish.name, selectedRestaurant.name, ServiceType.FOOD, dish.price, "Regular")
                            },
                            onDecrement = {
                                val firstCust = state.cartItems.firstOrNull { it.itemId == dish.id }?.customization ?: "Regular"
                                viewModel.updateCartQuantity(dish.id, firstCust, -1)
                            }
                        )
                    }
                }
            } else {
                val filteredRestaurants = state.restaurants.filter { r ->
                    (selectedCategory == "All" || r.category.equals(selectedCategory, true)) &&
                        (searchQuery.isBlank() || r.name.contains(searchQuery, true) || r.cuisine.contains(searchQuery, true))
                }
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search pizza, biryani, burgers, restaurants…") },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = PremiumPink) },
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("food_search_input")
                        )
                    }

                    // Special Offer Banner
                    item {
                        Glass3DCard(gradientBrush = CardPinkGradient, cornerRadius = 22.dp) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocalOffer, null, tint = PureWhite, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("MEGA FEAST DEALS", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text("Flat ₹50 OFF with SKYPULSE50", style = MaterialTheme.typography.titleLarge, color = PureWhite)
                                    Text("Combine Food + Grocery in 1 Multi-Service Cart!", style = MaterialTheme.typography.bodySmall, color = PureWhite.copy(alpha = 0.9f))
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.img_service_food),
                                    contentDescription = "Food Deals",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }
                    }

                    // 8 Food Categories
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(DemoDataSeeder.foodCategories) { cat ->
                                GlassChip(
                                    text = cat,
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat }
                                )
                            }
                        }
                    }

                    // Trending Dishes Quick Add
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Trending Food Right Now", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(state.foodMenuItems.take(5), key = { "trend_${it.id}" }) { dish ->
                                    val restName = state.restaurants.find { it.id == dish.restaurantId }?.name ?: "SkyPulse Kitchen"
                                    Glass3DCard(
                                        modifier = Modifier.width(196.dp),
                                        cornerRadius = 18.dp,
                                        contentPadding = PaddingValues(12.dp),
                                        onClick = { customizingItem = dish }
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            StatusPill(if (dish.isVeg) "VEG" else "NON-VEG", if (dish.isVeg) EmeraldSuccess else RoseError)
                                            Text(dish.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                                            Text(restName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("₹${dish.price}", style = MaterialTheme.typography.titleMedium, color = PremiumPink)
                                                Text("+ Add", style = MaterialTheme.typography.labelMedium, color = SkyBlue, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text("Popular & Nearby Restaurants (${filteredRestaurants.size})", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    }

                    items(filteredRestaurants, key = { it.id }) { rest ->
                        RestaurantListCard(
                            restaurant = rest,
                            onClick = { selectedRestaurantId = rest.id },
                            onToggleFav = { viewModel.toggleRestaurantFavourite(rest.id) }
                        )
                    }
                }
            }
        }

        // Sticky Multi-Service Cart Banner if items present
        if (state.totalCartItemsCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val total = state.cartItems.sumOf { it.unitPrice * it.quantity }
                GradientPillButton(
                    text = "View Multi-Service Cart (${state.totalCartItemsCount} items • ₹$total)",
                    icon = Icons.Default.ShoppingCart,
                    onClick = { viewModel.navigateTo(AppRoute.MultiCart) },
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "food_view_cart_button"
                )
            }
        }
    }

    // Dish Customization Dialog
    customizingItem?.let { dish ->
        val restName = state.restaurants.find { it.id == dish.restaurantId }?.name ?: "SkyPulse Kitchen"
        var chosenOption by remember(dish.id) { mutableStateOf(dish.customizations.first()) }
        val extraCost = when {
            chosenOption.contains("+₹35") -> 35
            chosenOption.contains("+₹15") -> 15
            else -> 0
        }
        AlertDialog(
            onDismissRequest = { customizingItem = null },
            title = { Text("Customize ${dish.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(dish.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    dish.customizations.forEach { opt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { chosenOption = opt }
                        ) {
                            RadioButton(selected = chosenOption == opt, onClick = { chosenOption = opt })
                            Text(opt, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addToCart(
                            itemId = dish.id,
                            title = dish.name,
                            sourceName = restName,
                            serviceType = ServiceType.FOOD,
                            unitPrice = dish.price + extraCost,
                            customization = chosenOption
                        )
                        customizingItem = null
                    }
                ) {
                    Text("Add • ₹${dish.price + extraCost}")
                }
            },
            dismissButton = {
                TextButton(onClick = { customizingItem = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun RestaurantListCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    onToggleFav: () -> Unit
) {
    Glass3DCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("restaurant_${restaurant.id}"),
        cornerRadius = 22.dp,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.img_service_food),
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(restaurant.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    IconButton(onClick = onToggleFav, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (restaurant.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favourite",
                            tint = PremiumPink
                        )
                    }
                }
                Text(restaurant.cuisine, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = AmberWarning, modifier = Modifier.size(14.dp))
                        Text(" ${restaurant.rating}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Text("• ${restaurant.deliveryMinutes} mins • Min ₹${restaurant.minOrder}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusPill(restaurant.offerTag, PremiumPink)
            }
        }
    }
}

@Composable
private fun FoodMenuItemCard(
    item: FoodMenuItem,
    quantityInCart: Int,
    onAddClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Glass3DCard(cornerRadius = 20.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(if (item.isVeg) "VEG" else "NON-VEG", if (item.isVeg) EmeraldSuccess else RoseError)
                    Text("★ ${item.rating}", style = MaterialTheme.typography.labelSmall, color = AmberWarning)
                }
                Text(item.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("₹${item.price}", style = MaterialTheme.typography.titleMedium, color = PremiumPink, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.width(10.dp))
            if (quantityInCart == 0) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    border = BorderStroke(1.dp, PremiumPink),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = onAddClick)
                        .testTag("add_food_${item.id}")
                ) {
                    Text(
                        text = "+ ADD",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkNavy)
                        .padding(horizontal = 4.dp)
                ) {
                    IconButton(onClick = onDecrement, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Remove, "Decrease", tint = PureWhite, modifier = Modifier.size(16.dp))
                    }
                    Text("$quantityInCart", style = MaterialTheme.typography.titleSmall, color = PureWhite)
                    IconButton(onClick = onIncrement, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Add, "Increase", tint = SkyBlue, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
