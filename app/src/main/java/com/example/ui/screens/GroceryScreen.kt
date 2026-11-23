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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AppRoute
import com.example.data.DemoDataSeeder
import com.example.data.ServiceType
import com.example.ui.components.Glass3DCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassSubScreenHeader
import com.example.ui.components.GradientPillButton
import com.example.ui.components.StatusPill
import com.example.ui.theme.CardMintSkyGradient
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.MutedNavy
import com.example.ui.theme.PaleSky
import com.example.ui.theme.PremiumPink
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyBlue
import com.example.viewmodel.SuperAppUiState
import com.example.viewmodel.SuperAppViewModel

@Composable
fun GroceryScreen(
    state: SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    BackHandler { viewModel.navigateBack() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var wishlistOnly by remember { mutableStateOf(false) }

    val slots = listOf("Express 15 mins", "Today 6–8 PM", "Tomorrow 7–9 AM")
    val filtered = state.groceryProducts.filter { p ->
        (selectedCategory == "All" || p.category.equals(selectedCategory, true)) &&
            (!wishlistOnly || p.isWishlisted) &&
            (searchQuery.isBlank() || p.name.contains(searchQuery, true) || p.category.contains(searchQuery, true))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("grocery_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlassSubScreenHeader(
                title = "SkyPulse Grocery",
                subtitle = "15-min Express & Fresh Daily Essentials",
                onBack = { viewModel.navigateBack() },
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

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 104.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search apples, A2 milk, sourdough, dishwash…") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = SkyBlue) },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("grocery_search_input")
                    )
                }

                // Express Slot & Promo Banner
                item {
                    Glass3DCard(gradientBrush = CardMintSkyGradient, cornerRadius = 22.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bolt, null, tint = PureWhite, modifier = Modifier.size(16.dp))
                                        Text("EXPRESS DELIVERY SLOT", style = MaterialTheme.typography.labelSmall, color = PureWhite)
                                    }
                                    Text("Freshness Guaranteed or Instant Refund", style = MaterialTheme.typography.titleMedium, color = PureWhite)
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.img_service_grocery),
                                    contentDescription = "Grocery Express",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(slots) { slot ->
                                    GlassChip(
                                        text = slot,
                                        selected = state.groceryDeliverySlot == slot,
                                        onClick = { viewModel.updateGrocerySlot(slot) }
                                    )
                                }
                            }
                        }
                    }
                }

                // 8 Categories + Wishlist filter
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            GlassChip(
                                text = if (wishlistOnly) "Wishlist ♥" else "Wishlist",
                                selected = wishlistOnly,
                                onClick = { wishlistOnly = !wishlistOnly }
                            )
                        }
                        items(DemoDataSeeder.groceryCategories) { cat ->
                            GlassChip(
                                text = cat,
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat }
                            )
                        }
                    }
                }

                // Product List
                items(filtered, key = { it.id }) { prod ->
                    val qty = state.cartItems.find { it.itemId == prod.id }?.quantity ?: 0
                    Glass3DCard(
                        cornerRadius = 20.dp,
                        modifier = Modifier.testTag("grocery_${prod.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_service_grocery),
                                contentDescription = prod.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(prod.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                    IconButton(
                                        onClick = { viewModel.toggleGroceryWishlist(prod.id) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (prod.isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Wishlist",
                                            tint = PremiumPink,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Text("${prod.unit} • ${prod.shopName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("₹${prod.price}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
                                    Text(
                                        text = "₹${prod.mrp}",
                                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    StatusPill(
                                        text = if (prod.stock > 0) "In Stock (${prod.stock})" else "Out of Stock",
                                        color = if (prod.stock > 0) EmeraldSuccess else RoseError
                                    )
                                }
                            }
                            Spacer(Modifier.width(6.dp))
                            if (qty == 0) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    border = BorderStroke(1.dp, SkyBlue),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable(enabled = prod.stock > 0) {
                                            viewModel.addToCart(prod.id, prod.name, prod.shopName, ServiceType.GROCERY, prod.price)
                                        }
                                        .testTag("add_grocery_${prod.id}")
                                ) {
                                    Text(
                                        text = "+ ADD",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(DarkNavy)
                                ) {
                                    IconButton(onClick = { viewModel.updateCartQuantity(prod.id, "", -1) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Remove, "Decrease", tint = PureWhite, modifier = Modifier.size(15.dp))
                                    }
                                    Text("$qty", style = MaterialTheme.typography.titleSmall, color = PureWhite)
                                    IconButton(onClick = { viewModel.updateCartQuantity(prod.id, "", 1) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Add, "Increase", tint = SkyBlue, modifier = Modifier.size(15.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.totalCartItemsCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val total = state.cartItems.sumOf { it.unitPrice * it.quantity }
                GradientPillButton(
                    text = "Checkout Multi-Service Cart (${state.totalCartItemsCount} • ₹$total)",
                    icon = Icons.Default.ShoppingCart,
                    onClick = { viewModel.navigateTo(AppRoute.MultiCart) },
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "grocery_checkout_button"
                )
            }
        }
    }
}
