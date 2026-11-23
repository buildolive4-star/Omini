package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiOperationalInsight
import com.example.ai.AiSmartRecommendation
import com.example.ai.SkyPulseAiService
import com.example.data.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

sealed interface AiFetchState<out T> {
    data object Idle : AiFetchState<Nothing>
    data object Loading : AiFetchState<Nothing>
    data class Success<T>(val data: T) : AiFetchState<T>
    data class Error(val message: String) : AiFetchState<Nothing>
}

data class SuperAppUiState(
    val currentRoute: AppRoute = AppRoute.Splash,
    val backStack: List<AppRoute> = emptyList(),
    val activeRole: UserRole = UserRole.CUSTOMER,
    val unlockedRoles: Set<UserRole> = setOf(UserRole.CUSTOMER),
    val isDarkGlassMode: Boolean = false,
    val snackbarMessage: String? = null,
    val globalSearchQuery: String = "",
    // Customer core data
    val profile: CustomerProfile = DemoDataSeeder.initialProfile,
    val addresses: List<SavedAddress> = DemoDataSeeder.initialAddresses,
    val familyMembers: List<FamilyMember> = DemoDataSeeder.initialFamilyMembers,
    val activeFamilyMemberId: String? = null,
    val businessProfile: BusinessProfile = DemoDataSeeder.initialBusinessProfile,
    val walletBalance: Int = 2480,
    val transactions: List<WalletTransaction> = DemoDataSeeder.initialTransactions,
    val coupons: List<CouponItem> = DemoDataSeeder.initialCoupons,
    val appliedCouponCode: String = "",
    val selectedPaymentMethod: String = "SkyPulse Wallet",
    val notifications: List<NotificationItem> = DemoDataSeeder.initialNotifications,
    val reviews: List<ReviewItem> = DemoDataSeeder.initialReviews,
    // Ride module
    val activeRide: ActiveRide? = null,
    // Food & Grocery & Multi-Service Cart
    val restaurants: List<Restaurant> = DemoDataSeeder.restaurants,
    val foodMenuItems: List<FoodMenuItem> = DemoDataSeeder.foodMenuItems,
    val groceryProducts: List<GroceryProduct> = DemoDataSeeder.groceryProducts,
    val cartItems: List<CartItem> = emptyList(),
    val groceryDeliverySlot: String = "Express 15 mins",
    // Unified Orders
    val orders: List<OrderRecord> = DemoDataSeeder.initialOrders,
    // Partner Dashboard
    val partnerProfile: PartnerKycProfile = DemoDataSeeder.initialPartnerProfile,
    val partnerJobs: List<PartnerJobRequest> = DemoDataSeeder.initialPartnerJobs,
    // Restaurant Owner Panel
    val restaurantTickets: List<MerchantOrderTicket> = DemoDataSeeder.initialRestaurantTickets,
    // Grocery Shop Owner Panel
    val shopTickets: List<MerchantOrderTicket> = DemoDataSeeder.initialShopTickets,
    // Admin Panel
    val adminConfig: AdminPlatformConfig = DemoDataSeeder.initialAdminConfig,
    val fraudAlerts: List<FraudAlert> = DemoDataSeeder.initialFraudAlerts,
    val complaints: List<SupportComplaint> = DemoDataSeeder.initialComplaints,
    // Gemini AI Features
    val aiChatMessages: List<AiChatEntry> = listOf(
        AiChatEntry(
            id = "ai_welcome",
            text = "Hi! I'm SkyPulse AI powered by Gemini. Ask me to plan a quick ride, pick dinner under ₹300, track your live parcel, or optimize deliveries!",
            isUser = false
        )
    ),
    val aiChatLoading: Boolean = false,
    val aiChatError: String? = null,
    val aiRecommendationsState: AiFetchState<List<AiSmartRecommendation>> = AiFetchState.Idle,
    val aiOpsInsightState: AiFetchState<AiOperationalInsight> = AiFetchState.Idle
) {
    val unreadNotificationsCount: Int get() = notifications.count { !it.isRead }
    val totalCartItemsCount: Int get() = cartItems.sumOf { it.quantity }
    val liveOrder: OrderRecord? get() = orders.firstOrNull { it.isLive }
}

class SuperAppViewModel : ViewModel() {

    private val aiService = SkyPulseAiService()
    private val _uiState = MutableStateFlow(SuperAppUiState())
    val uiState: StateFlow<SuperAppUiState> = _uiState.asStateFlow()

    private var rideSimulationJob: Job? = null

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun showMessage(msg: String) {
        _uiState.update { it.copy(snackbarMessage = msg) }
    }

    fun toggleDarkGlassTheme() {
        _uiState.update { state ->
            val next = !state.isDarkGlassMode
            state.copy(
                isDarkGlassMode = next,
                snackbarMessage = if (next) "Cyber Midnight 3D Glass Theme ON" else "Pearl Sky-Pink 3D Glass Theme ON"
            )
        }
    }

    // Navigation & Backstack
    fun finishSplash() {
        _uiState.update { it.copy(currentRoute = AppRoute.Home, backStack = emptyList()) }
    }

    fun navigateTo(route: AppRoute, clearStack: Boolean = false) {
        _uiState.update { state ->
            val targetRoute = when (route) {
                AppRoute.PartnerDashboard -> if (UserRole.DRIVER_PARTNER in state.unlockedRoles) route else AppRoute.RoleAuthGate(UserRole.DRIVER_PARTNER)
                AppRoute.RestaurantPanel -> if (UserRole.RESTAURANT_OWNER in state.unlockedRoles) route else AppRoute.RoleAuthGate(UserRole.RESTAURANT_OWNER)
                AppRoute.ShopPanel -> if (UserRole.GROCERY_OWNER in state.unlockedRoles) route else AppRoute.RoleAuthGate(UserRole.GROCERY_OWNER)
                AppRoute.AdminPanel -> if (UserRole.ADMIN in state.unlockedRoles) route else AppRoute.RoleAuthGate(UserRole.ADMIN)
                AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD) ->
                    if (UserRole.BUSINESS_ACCOUNT in state.unlockedRoles) route else AppRoute.RoleAuthGate(UserRole.BUSINESS_ACCOUNT)
                else -> route
            }
            if (targetRoute == state.currentRoute) return@update state
            val newStack = if (clearStack) emptyList() else (state.backStack + state.currentRoute).takeLast(12)
            state.copy(currentRoute = targetRoute, backStack = newStack)
        }
    }

    fun navigateBack(): Boolean {
        val state = _uiState.value
        if (state.backStack.isNotEmpty()) {
            val previous = state.backStack.last()
            _uiState.update {
                it.copy(currentRoute = previous, backStack = it.backStack.dropLast(1))
            }
            return true
        }
        if (state.currentRoute != AppRoute.Home && state.currentRoute != AppRoute.Splash) {
            _uiState.update { it.copy(currentRoute = AppRoute.Home) }
            return true
        }
        return false
    }

    fun authenticateRole(role: UserRole, pinInput: String, identifier: String): Boolean {
        if (identifier.isBlank() || pinInput.trim() != role.demoCode) {
            showMessage("Invalid credentials. Use demo access PIN ${role.demoCode} for ${role.title}.")
            return false
        }
        val dest = when (role) {
            UserRole.CUSTOMER -> AppRoute.Home
            UserRole.DRIVER_PARTNER -> AppRoute.PartnerDashboard
            UserRole.RESTAURANT_OWNER -> AppRoute.RestaurantPanel
            UserRole.GROCERY_OWNER -> AppRoute.ShopPanel
            UserRole.BUSINESS_ACCOUNT -> AppRoute.CustomerDetail(CustomerSection.BUSINESS_DASHBOARD)
            UserRole.ADMIN -> AppRoute.AdminPanel
        }
        _uiState.update {
            it.copy(
                activeRole = role,
                unlockedRoles = it.unlockedRoles + role,
                currentRoute = dest,
                snackbarMessage = "Authorized as ${role.title}"
            )
        }
        return true
    }

    fun logoutRolePanels() {
        _uiState.update {
            it.copy(
                activeRole = UserRole.CUSTOMER,
                unlockedRoles = setOf(UserRole.CUSTOMER),
                currentRoute = AppRoute.Home,
                backStack = emptyList(),
                snackbarMessage = "Signed out of privileged panels. Returned to Customer mode."
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(globalSearchQuery = query) }
    }

    fun updateLocation(newLocation: String) {
        val city = when {
            newLocation.contains("Mumbai", true) -> "Mumbai"
            newLocation.contains("Delhi", true) -> "Delhi NCR"
            else -> "Bengaluru"
        }
        _uiState.update {
            it.copy(
                profile = it.profile.copy(currentLocation = newLocation, currentCity = city),
                snackbarMessage = "Location updated to $newLocation"
            )
        }
    }

    fun markAllNotificationsRead() {
        _uiState.update { state ->
            state.copy(notifications = state.notifications.map { it.copy(isRead = true) })
        }
    }

    // ==================== RIDE BOOKING ====================

    fun bookRide(pickup: String, drop: String, vehicle: RideVehicleOption, paymentMethod: String) {
        val surge = _uiState.value.adminConfig.surgeMultiplier
        val distanceKm = ((pickup.length + drop.length) % 9 + 3.4).let { (it * 10).roundToInt() / 10.0 }
        val estimatedFare = ((vehicle.baseFare + distanceKm * vehicle.perKmRate) * surge).roundToInt()
        if (paymentMethod == "SkyPulse Wallet" && _uiState.value.walletBalance < estimatedFare) {
            showMessage("Insufficient Wallet balance. Top up Wallet or pick UPI/Cash.")
            return
        }
        val driver = when (vehicle.id) {
            "bike" -> DemoDataSeeder.sampleDrivers[0]
            "auto" -> DemoDataSeeder.sampleDrivers[1]
            "suv" -> DemoDataSeeder.sampleDrivers[3]
            else -> DemoDataSeeder.sampleDrivers[2]
        }
        val otp = Random.nextInt(1000, 9999).toString()
        val rideId = "SP-RD-${Random.nextInt(1000, 9999)}"
        val activeRide = ActiveRide(
            id = rideId,
            pickup = pickup,
            drop = drop,
            distanceKm = distanceKm,
            vehicle = vehicle,
            fare = estimatedFare,
            paymentMethod = paymentMethod,
            otp = otp,
            driver = driver,
            phase = RidePhase.SEARCHING,
            progress = 0.05f,
            chatMessages = listOf(
                ChatMessage("cm_1", driver.name, "Namaste! I'm heading to your pickup point now.", false, "Just now")
            )
        )
        val partnerCat = if (vehicle.id == "bike") PartnerCategory.BIKE_TAXI else PartnerCategory.AUTO_CAB
        val partnerJob = PartnerJobRequest(
            id = "JOB-${Random.nextInt(100, 999)}",
            category = partnerCat,
            customerName = _uiState.value.profile.name,
            customerPhone = _uiState.value.profile.phone,
            pickup = pickup,
            drop = drop,
            distanceKm = distanceKm,
            payout = (estimatedFare * 0.85).roundToInt(),
            pickupOtp = otp,
            deliveryOtp = otp,
            status = PartnerJobStatus.PENDING,
            itemSummary = "${vehicle.name} Ride"
        )
        val orderRecord = OrderRecord(
            id = rideId,
            serviceType = ServiceType.RIDE,
            title = "${vehicle.name} • ${pickup.substringBefore(",")} → ${drop.substringBefore(",")}",
            subtitle = "Captain ${driver.name} • ${driver.vehiclePlate}",
            amount = estimatedFare,
            status = "Matching Captain",
            statusStep = 1,
            totalSteps = 4,
            timestamp = "Just now",
            paymentMethod = paymentMethod,
            etaText = "Arriving in ${vehicle.etaMinutes} mins",
            pickupOtp = otp,
            isLive = true
        )

        _uiState.update { state ->
            val updatedBalance = if (paymentMethod == "SkyPulse Wallet") state.walletBalance - estimatedFare else state.walletBalance
            val updatedTx = if (paymentMethod == "SkyPulse Wallet") {
                listOf(WalletTransaction("tx_${System.currentTimeMillis()}", "${vehicle.name} Ride", "Ride", estimatedFare, false, "Just now", "Paid")) + state.transactions
            } else state.transactions

            state.copy(
                activeRide = activeRide,
                walletBalance = updatedBalance,
                transactions = updatedTx,
                orders = listOf(orderRecord) + state.orders.map { if (it.serviceType == ServiceType.RIDE) it.copy(isLive = false) else it },
                partnerJobs = listOf(partnerJob) + state.partnerJobs,
                snackbarMessage = "Ride confirmed! Share OTP $otp with Captain ${driver.name}"
            )
        }
        startRideSimulation()
    }

    private fun startRideSimulation() {
        rideSimulationJob?.cancel()
        rideSimulationJob = viewModelScope.launch {
            delay(1600)
            _uiState.update { state ->
                val ride = state.activeRide ?: return@update state
                state.copy(
                    activeRide = ride.copy(phase = RidePhase.DRIVER_ASSIGNED, progress = 0.25f),
                    orders = state.orders.map { if (it.id == ride.id) it.copy(status = "Captain Arriving", statusStep = 2) else it }
                )
            }
        }
    }

    fun verifyRideOtpAndStartTrip() {
        val ride = _uiState.value.activeRide ?: return
        _uiState.update { state ->
            state.copy(
                activeRide = ride.copy(phase = RidePhase.OTP_VERIFIED, progress = 0.52f),
                orders = state.orders.map { if (it.id == ride.id) it.copy(status = "On Trip", statusStep = 3, etaText = "6 mins to destination") else it },
                snackbarMessage = "OTP Verified! Simulated live trip tracking started."
            )
        }
        rideSimulationJob?.cancel()
        rideSimulationJob = viewModelScope.launch {
            listOf(0.65f, 0.78f, 0.90f, 1.0f).forEach { p ->
                delay(1800)
                _uiState.update { state ->
                    val current = state.activeRide ?: return@update state
                    if (current.phase != RidePhase.OTP_VERIFIED) return@update state
                    if (p >= 1.0f) {
                        state.copy(
                            activeRide = current.copy(phase = RidePhase.COMPLETED, progress = 1.0f),
                            orders = state.orders.map { if (it.id == current.id) it.copy(status = "Completed", statusStep = 4, etaText = "Arrived safely", isLive = false) else it }
                        )
                    } else {
                        state.copy(activeRide = current.copy(progress = p))
                    }
                }
            }
        }
    }

    fun sendDriverChatMessage(text: String) {
        if (text.isBlank()) return
        val ride = _uiState.value.activeRide ?: return
        val userMsg = ChatMessage("cm_${System.currentTimeMillis()}", "You", text.trim(), true, "Now")
        val replyMsg = ChatMessage("cm_${System.currentTimeMillis() + 1}", ride.driver.name, "Got it! I'm just 2 minutes away near the main gate.", false, "Now")
        _uiState.update { state ->
            state.copy(activeRide = ride.copy(chatMessages = ride.chatMessages + userMsg + replyMsg))
        }
    }

    fun rateAndCloseRide(stars: Int, feedback: String) {
        val ride = _uiState.value.activeRide ?: return
        val review = ReviewItem(
            id = "rev_${System.currentTimeMillis()}",
            serviceType = ServiceType.RIDE,
            targetName = "Captain ${ride.driver.name} (${ride.vehicle.name})",
            rating = stars,
            comment = feedback.ifBlank { "Smooth & punctual ride!" },
            date = "Today"
        )
        _uiState.update { state ->
            state.copy(
                activeRide = null,
                reviews = listOf(review) + state.reviews,
                snackbarMessage = "Thanks for rating Captain ${ride.driver.name} ($stars★)!"
            )
        }
    }

    fun cancelActiveRide() {
        rideSimulationJob?.cancel()
        val ride = _uiState.value.activeRide ?: return
        _uiState.update { state ->
            state.copy(
                activeRide = null,
                orders = state.orders.map { if (it.id == ride.id) it.copy(status = "Cancelled", isLive = false) else it },
                snackbarMessage = "Ride ${ride.id} cancelled."
            )
        }
    }

    // ==================== FOOD, GROCERY & MULTI-SERVICE CART ====================

    fun toggleRestaurantFavourite(restaurantId: String) {
        _uiState.update { state ->
            state.copy(
                restaurants = state.restaurants.map {
                    if (it.id == restaurantId) it.copy(isFavourite = !it.isFavourite) else it
                }
            )
        }
    }

    fun toggleGroceryWishlist(productId: String) {
        _uiState.update { state ->
            state.copy(
                groceryProducts = state.groceryProducts.map {
                    if (it.id == productId) it.copy(isWishlisted = !it.isWishlisted) else it
                }
            )
        }
    }

    fun addToCart(
        itemId: String,
        title: String,
        sourceName: String,
        serviceType: ServiceType,
        unitPrice: Int,
        customization: String = ""
    ) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.itemId == itemId && it.customization == customization }
            val updated = if (existing != null) {
                state.cartItems.map {
                    if (it.itemId == itemId && it.customization == customization) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                state.cartItems + CartItem(itemId, title, sourceName, serviceType, unitPrice, 1, customization)
            }
            state.copy(cartItems = updated, snackbarMessage = "Added $title to Multi-Service Cart")
        }
    }

    fun updateCartQuantity(itemId: String, customization: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.itemId == itemId && item.customization == customization) {
                    val nextQty = item.quantity + delta
                    if (nextQty <= 0) null else item.copy(quantity = nextQty)
                } else item
            }
            state.copy(cartItems = updated)
        }
    }

    fun updateGrocerySlot(slot: String) {
        _uiState.update { it.copy(groceryDeliverySlot = slot) }
    }

    fun applyCoupon(code: String) {
        val clean = code.trim().uppercase()
        val coupon = _uiState.value.coupons.find { it.code.equals(clean, ignoreCase = true) }
        if (coupon == null) {
            showMessage("Coupon '$clean' not found. Try SKYPULSE50 or FIRSTFREE.")
        } else {
            _uiState.update { it.copy(appliedCouponCode = coupon.code, snackbarMessage = "Coupon ${coupon.code} applied!") }
        }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun placeMultiServiceCartOrder() {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) {
            showMessage("Your cart is empty.")
            return
        }
        val subtotal = state.cartItems.sumOf { it.unitPrice * it.quantity }
        val deliveryFee = state.adminConfig.baseDeliveryFee
        val taxes = (subtotal * 0.05).roundToInt()
        val coupon = state.coupons.find { it.code == state.appliedCouponCode }
        val discount = if (coupon != null && subtotal >= coupon.minOrder) coupon.discountFlat else 0
        val grandTotal = (subtotal + deliveryFee + taxes - discount).coerceAtLeast(1)

        if (state.selectedPaymentMethod == "SkyPulse Wallet" && state.walletBalance < grandTotal) {
            showMessage("Insufficient Wallet balance (₹${state.walletBalance}). Top up or choose UPI.")
            return
        }

        val hasFood = state.cartItems.any { it.serviceType == ServiceType.FOOD }
        val hasGrocery = state.cartItems.any { it.serviceType == ServiceType.GROCERY }
        val primaryType = if (hasFood) ServiceType.FOOD else ServiceType.GROCERY
        val title = when {
            hasFood && hasGrocery -> "Multi-Service Cart (Food + Grocery)"
            else -> state.cartItems.first().sourceName
        }
        val summary = state.cartItems.joinToString(", ") { "${it.quantity}x ${it.title}" }
        val orderId = "SP-MC-${Random.nextInt(1000, 9999)}"
        val otp = Random.nextInt(1000, 9999).toString()

        val newOrder = OrderRecord(
            id = orderId,
            serviceType = primaryType,
            title = title,
            subtitle = summary,
            amount = grandTotal,
            status = "Order Placed",
            statusStep = 1,
            totalSteps = 6,
            timestamp = "Just now",
            paymentMethod = state.selectedPaymentMethod,
            etaText = if (hasGrocery && !hasFood) state.groceryDeliverySlot else "Arriving in 26 mins",
            deliveryOtp = otp,
            isLive = true
        )

        val newRestaurantTicket = if (hasFood) {
            MerchantOrderTicket("RO-${Random.nextInt(400, 999)}", state.profile.name, summary, grandTotal, 20, "Incoming", "Just now")
        } else null
        val newShopTicket = if (hasGrocery) {
            MerchantOrderTicket("SO-${Random.nextInt(500, 999)}", state.profile.name, summary, grandTotal, 10, "Incoming", "Just now")
        } else null
        val newPartnerJob = PartnerJobRequest(
            id = "JOB-${Random.nextInt(100, 999)}",
            category = if (hasFood) PartnerCategory.FOOD_DELIVERY else PartnerCategory.GROCERY_DELIVERY,
            customerName = state.profile.name,
            customerPhone = state.profile.phone,
            pickup = state.cartItems.first().sourceName,
            drop = state.profile.currentLocation,
            distanceKm = 3.2,
            payout = 75,
            pickupOtp = Random.nextInt(1000, 9999).toString(),
            deliveryOtp = otp,
            status = PartnerJobStatus.PENDING,
            itemSummary = summary.take(45)
        )

        _uiState.update { s ->
            val newBal = if (s.selectedPaymentMethod == "SkyPulse Wallet") s.walletBalance - grandTotal else s.walletBalance
            val newTx = if (s.selectedPaymentMethod == "SkyPulse Wallet") {
                listOf(WalletTransaction("tx_${System.currentTimeMillis()}", title, "Cart Checkout", grandTotal, false, "Just now", "Paid")) + s.transactions
            } else s.transactions

            s.copy(
                cartItems = emptyList(),
                appliedCouponCode = "",
                walletBalance = newBal,
                transactions = newTx,
                orders = listOf(newOrder) + s.orders,
                restaurantTickets = if (newRestaurantTicket != null) listOf(newRestaurantTicket) + s.restaurantTickets else s.restaurantTickets,
                shopTickets = if (newShopTicket != null) listOf(newShopTicket) + s.shopTickets else s.shopTickets,
                partnerJobs = listOf(newPartnerJob) + s.partnerJobs,
                currentRoute = AppRoute.Orders,
                snackbarMessage = "Order $orderId placed! Live tracking active."
            )
        }
    }

    fun advanceOrderStatus(orderId: String) {
        _uiState.update { state ->
            state.copy(
                orders = state.orders.map { order ->
                    if (order.id != orderId) return@map order
                    val nextStep = (order.statusStep + 1).coerceAtMost(order.totalSteps)
                    val labels = when (order.serviceType) {
                        ServiceType.FOOD -> listOf("Order Placed", "Restaurant Accepted", "Food Preparing", "Picked Up", "On the way", "Delivered")
                        ServiceType.GROCERY -> listOf("Placed", "Packed", "Out for Delivery", "Delivered")
                        ServiceType.PARCEL -> listOf("Booked", "Partner Matched", "Picked Up (OTP)", "In Transit", "Delivered (POD)")
                        ServiceType.TRANSPORT -> listOf("Booked", "Loading", "In Transit", "Unloaded & Done")
                        ServiceType.RIDE -> listOf("Searching", "Captain Arriving", "On Trip", "Completed")
                    }
                    val statusLabel = labels.getOrElse((nextStep - 1).coerceAtLeast(0)) { "Completed" }
                    order.copy(
                        statusStep = nextStep,
                        status = statusLabel,
                        isLive = nextStep < order.totalSteps,
                        etaText = if (nextStep >= order.totalSteps) "Completed!" else "Updated live"
                    )
                }
            )
        }
    }

    fun requestGroceryRefund(orderId: String) {
        val target = _uiState.value.orders.find { it.id == orderId } ?: return
        val refundAmt = (target.amount * 0.25).roundToInt().coerceAtLeast(35)
        _uiState.update { state ->
            state.copy(
                walletBalance = state.walletBalance + refundAmt,
                orders = state.orders.map { if (it.id == orderId) it.copy(isRefundRequested = true) else it },
                transactions = listOf(
                    WalletTransaction("tx_${System.currentTimeMillis()}", "Refund for $orderId", "Refund", refundAmt, true, "Just now", "Credited")
                ) + state.transactions,
                snackbarMessage = "Instant demo refund of ₹$refundAmt credited to SkyPulse Wallet!"
            )
        }
    }

    // ==================== PARCEL & GOODS TRANSPORT ====================

    fun bookParcel(
        pickup: String,
        drop: String,
        senderName: String,
        receiverName: String,
        category: String,
        weightKg: String,
        isScheduled: Boolean,
        vehicleName: String,
        estimatedFare: Int
    ) {
        val id = "SP-PR-${Random.nextInt(1000, 9999)}"
        val pOtp = Random.nextInt(1000, 9999).toString()
        val dOtp = Random.nextInt(1000, 9999).toString()
        val order = OrderRecord(
            id = id,
            serviceType = ServiceType.PARCEL,
            title = "Parcel ($category) • $vehicleName",
            subtitle = "$senderName → $receiverName ($weightKg kg)",
            amount = estimatedFare,
            status = if (isScheduled) "Scheduled" else "Partner Matched",
            statusStep = 2,
            totalSteps = 5,
            timestamp = "Just now",
            paymentMethod = _uiState.value.selectedPaymentMethod,
            etaText = if (isScheduled) "Scheduled Today 5:00 PM" else "Pickup in 8 mins",
            pickupOtp = pOtp,
            deliveryOtp = dOtp,
            isLive = true
        )
        val job = PartnerJobRequest(
            id = "JOB-${Random.nextInt(100, 999)}",
            category = PartnerCategory.PARCEL_DELIVERY,
            customerName = senderName,
            customerPhone = _uiState.value.profile.phone,
            pickup = pickup,
            drop = drop,
            distanceKm = 6.5,
            payout = (estimatedFare * 0.82).roundToInt(),
            pickupOtp = pOtp,
            deliveryOtp = dOtp,
            status = PartnerJobStatus.PENDING,
            itemSummary = "$category ($weightKg kg) for $receiverName"
        )
        _uiState.update { state ->
            state.copy(
                orders = listOf(order) + state.orders,
                partnerJobs = listOf(job) + state.partnerJobs,
                currentRoute = AppRoute.Orders,
                snackbarMessage = "Parcel $id booked! Pickup OTP: $pOtp | Drop OTP: $dOtp"
            )
        }
    }

    fun bookTransport(
        pickup: String,
        drop: String,
        vehicle: RideVehicleOption,
        goodsType: String,
        weightKg: String,
        loadingAssist: Boolean,
        unloadingAssist: Boolean,
        helpersCount: Int,
        scheduleText: String,
        totalFare: Int
    ) {
        val id = "SP-TR-${Random.nextInt(1000, 9999)}"
        val assistNote = buildList {
            if (loadingAssist) add("Loading")
            if (unloadingAssist) add("Unloading")
            if (helpersCount > 0) add("$helpersCount Helpers")
        }.joinToString(" • ").ifEmpty { "Self Load" }

        val order = OrderRecord(
            id = id,
            serviceType = ServiceType.TRANSPORT,
            title = "${vehicle.name} • $goodsType",
            subtitle = "$pickup → $drop ($weightKg kg • $assistNote)",
            amount = totalFare,
            status = "Vehicle Assigned",
            statusStep = 1,
            totalSteps = 4,
            timestamp = "Just now",
            paymentMethod = _uiState.value.selectedPaymentMethod,
            etaText = scheduleText,
            pickupOtp = Random.nextInt(1000, 9999).toString(),
            isLive = true
        )
        val job = PartnerJobRequest(
            id = "JOB-${Random.nextInt(100, 999)}",
            category = PartnerCategory.GOODS_TRANSPORT,
            customerName = _uiState.value.profile.name,
            customerPhone = _uiState.value.profile.phone,
            pickup = pickup,
            drop = drop,
            distanceKm = 12.0,
            payout = (totalFare * 0.85).roundToInt(),
            pickupOtp = order.pickupOtp,
            deliveryOtp = Random.nextInt(1000, 9999).toString(),
            status = PartnerJobStatus.PENDING,
            itemSummary = "${vehicle.name}: $goodsType ($assistNote)"
        )
        _uiState.update { state ->
            state.copy(
                orders = listOf(order) + state.orders,
                partnerJobs = listOf(job) + state.partnerJobs,
                currentRoute = AppRoute.Orders,
                snackbarMessage = "Transport $id confirmed with ${vehicle.name}!"
            )
        }
    }

    // ==================== ONE-TAP REBOOKING ====================

    fun oneTapRebook(order: OrderRecord) {
        val newId = "SP-RB-${Random.nextInt(1000, 9999)}"
        val clone = order.copy(
            id = newId,
            status = "Rebooked • Live",
            statusStep = 1,
            timestamp = "Just now",
            etaText = "Processing now",
            isLive = true,
            isRefundRequested = false
        )
        _uiState.update { state ->
            state.copy(
                orders = listOf(clone) + state.orders,
                snackbarMessage = "One-Tap Rebook confirmed! New ID: $newId"
            )
        }
    }

    // ==================== WALLET, PROFILE, ADDRESSES, FAMILY ====================

    fun addMoneyToWallet(amount: Int) {
        if (amount <= 0) return
        _uiState.update { state ->
            state.copy(
                walletBalance = state.walletBalance + amount,
                transactions = listOf(
                    WalletTransaction("tx_${System.currentTimeMillis()}", "Wallet Top-up (UPI)", "Top-up", amount, true, "Just now", "Success")
                ) + state.transactions,
                snackbarMessage = "₹$amount added to SkyPulse Wallet!"
            )
        }
    }

    fun updateCustomerProfile(name: String, phone: String, email: String) {
        val initials = name.trim().split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }.ifBlank { "SP" }
        _uiState.update { state ->
            state.copy(
                profile = state.profile.copy(name = name, phone = phone, email = email, avatarInitials = initials),
                snackbarMessage = "Profile updated successfully."
            )
        }
    }

    fun addSavedAddress(label: String, fullAddress: String, tag: String) {
        if (label.isBlank() || fullAddress.isBlank()) return
        val item = SavedAddress("addr_${System.currentTimeMillis()}", label, fullAddress, tag)
        _uiState.update { state ->
            state.copy(addresses = state.addresses + item, snackbarMessage = "Address '$label' saved!")
        }
    }

    fun addFamilyMember(name: String, relation: String, phone: String, address: String, limit: Int) {
        if (name.isBlank()) return
        val member = FamilyMember("fam_${System.currentTimeMillis()}", name, relation, phone, address, limit)
        _uiState.update { state ->
            state.copy(familyMembers = state.familyMembers + member, snackbarMessage = "$name added to Family Account!")
        }
    }

    fun selectFamilyProfile(memberId: String?) {
        _uiState.update { state ->
            val member = state.familyMembers.find { it.id == memberId }
            state.copy(
                activeFamilyMemberId = memberId,
                snackbarMessage = if (member != null) "Booking for ${member.name} (${member.relation})" else "Switched back to Personal Account"
            )
        }
    }

    // ==================== PARTNER / DRIVER DASHBOARD ====================

    fun togglePartnerOnline() {
        _uiState.update { state ->
            val next = !state.partnerProfile.isOnline
            state.copy(
                partnerProfile = state.partnerProfile.copy(isOnline = next),
                snackbarMessage = if (next) "You are now ONLINE for ${state.partnerProfile.category.label} jobs!" else "You are now OFFLINE."
            )
        }
    }

    fun selectPartnerCategory(category: PartnerCategory) {
        _uiState.update { state ->
            state.copy(
                partnerProfile = state.partnerProfile.copy(category = category),
                snackbarMessage = "Showing verified ${category.label} requests only."
            )
        }
    }

    fun acceptPartnerJob(jobId: String) {
        _uiState.update { state ->
            state.copy(
                partnerJobs = state.partnerJobs.map {
                    if (it.id == jobId) it.copy(status = PartnerJobStatus.ACCEPTED) else it
                },
                snackbarMessage = "Job $jobId accepted! Navigate to pickup point."
            )
        }
    }

    fun rejectPartnerJob(jobId: String) {
        _uiState.update { state ->
            state.copy(
                partnerJobs = state.partnerJobs.filterNot { it.id == jobId },
                snackbarMessage = "Job $jobId declined."
            )
        }
    }

    fun advancePartnerJob(jobId: String) {
        _uiState.update { state ->
            val job = state.partnerJobs.find { it.id == jobId } ?: return@update state
            val nextStatus = when (job.status) {
                PartnerJobStatus.PENDING -> PartnerJobStatus.ACCEPTED
                PartnerJobStatus.ACCEPTED -> PartnerJobStatus.PICKED_UP
                PartnerJobStatus.PICKED_UP -> PartnerJobStatus.COMPLETED
                PartnerJobStatus.COMPLETED -> PartnerJobStatus.COMPLETED
            }
            val earned = if (nextStatus == PartnerJobStatus.COMPLETED && job.status != PartnerJobStatus.COMPLETED) job.payout else 0
            val completedDelta = if (earned > 0) 1 else 0
            state.copy(
                partnerJobs = state.partnerJobs.map { if (it.id == jobId) it.copy(status = nextStatus) else it },
                partnerProfile = state.partnerProfile.copy(
                    todayEarnings = state.partnerProfile.todayEarnings + earned,
                    completedToday = state.partnerProfile.completedToday + completedDelta
                ),
                snackbarMessage = if (earned > 0) "Job completed! +₹$earned added to today's earnings." else "Status updated: ${nextStatus.label}"
            )
        }
    }

    fun submitPartnerKyc(
        name: String,
        mobile: String,
        category: PartnerCategory,
        aadhaar: String,
        pan: String,
        dl: String,
        rc: String,
        insurance: String,
        bank: String
    ) {
        _uiState.update { state ->
            state.copy(
                partnerProfile = state.partnerProfile.copy(
                    name = name,
                    mobile = mobile,
                    category = category,
                    aadhaarNumber = aadhaar,
                    panNumber = pan,
                    drivingLicence = dl,
                    vehicleRc = rc,
                    vehicleInsurance = insurance,
                    bankAccount = bank,
                    documentsUploaded = true,
                    verificationStatus = "Pending Admin Review"
                ),
                snackbarMessage = "Partner KYC submitted for Admin verification!"
            )
        }
    }

    // ==================== RESTAURANT & GROCERY OWNER PANELS ====================

    fun updateRestaurantTicketStatus(ticketId: String, newStatus: String, prepMinutes: Int? = null) {
        _uiState.update { state ->
            state.copy(
                restaurantTickets = state.restaurantTickets.map {
                    if (it.id == ticketId) it.copy(status = newStatus, prepMinutes = prepMinutes ?: it.prepMinutes) else it
                },
                snackbarMessage = "Restaurant order $ticketId → $newStatus"
            )
        }
    }

    fun addRestaurantMenuItem(name: String, desc: String, category: String, price: Int, isVeg: Boolean) {
        if (name.isBlank() || price <= 0) return
        val newItem = FoodMenuItem("f_${System.currentTimeMillis()}", "rest_1", name, desc, category, price, isVeg, 4.7, true)
        _uiState.update { state ->
            state.copy(foodMenuItems = listOf(newItem) + state.foodMenuItems, snackbarMessage = "Added '$name' to live menu!")
        }
    }

    fun toggleMenuItemAvailability(itemId: String) {
        _uiState.update { state ->
            state.copy(foodMenuItems = state.foodMenuItems.map { if (it.id == itemId) it.copy(isAvailable = !it.isAvailable) else it })
        }
    }

    fun deleteMenuItem(itemId: String) {
        _uiState.update { state ->
            state.copy(foodMenuItems = state.foodMenuItems.filterNot { it.id == itemId }, snackbarMessage = "Menu item removed.")
        }
    }

    fun updateShopTicketStatus(ticketId: String, newStatus: String, driverName: String? = null) {
        _uiState.update { state ->
            state.copy(
                shopTickets = state.shopTickets.map {
                    if (it.id == ticketId) it.copy(status = newStatus, assignedDriver = driverName ?: it.assignedDriver) else it
                },
                snackbarMessage = "Grocery order $ticketId → $newStatus"
            )
        }
    }

    fun adjustGroceryStock(productId: String, delta: Int) {
        _uiState.update { state ->
            state.copy(
                groceryProducts = state.groceryProducts.map {
                    if (it.id == productId) it.copy(stock = (it.stock + delta).coerceAtLeast(0)) else it
                }
            )
        }
    }

    fun addGroceryProduct(name: String, category: String, unit: String, price: Int, stock: Int) {
        if (name.isBlank() || price <= 0) return
        val item = GroceryProduct("g_${System.currentTimeMillis()}", "FreshMart Express", name, category, unit, price, price + 20, stock, 4.7)
        _uiState.update { state ->
            state.copy(groceryProducts = listOf(item) + state.groceryProducts, snackbarMessage = "Added '$name' to shop inventory!")
        }
    }

    // ==================== ADMIN CONTROLS ====================

    fun updateAdminPricing(commission: Double, deliveryFee: Int, surge: Double, dynamicPricing: Boolean) {
        _uiState.update { state ->
            state.copy(
                adminConfig = state.adminConfig.copy(
                    commissionPercent = commission,
                    baseDeliveryFee = deliveryFee,
                    surgeMultiplier = surge,
                    dynamicPricingEnabled = dynamicPricing
                )
            )
        }
    }

    fun toggleAdminCity(city: String) {
        _uiState.update { state ->
            val current = state.adminConfig.activeCities[city] ?: false
            state.copy(
                adminConfig = state.adminConfig.copy(
                    activeCities = state.adminConfig.activeCities + (city to !current)
                ),
                snackbarMessage = "$city service ${if (!current) "enabled" else "paused"}."
            )
        }
    }

    fun approvePartnerKyc() {
        _uiState.update { state ->
            state.copy(
                partnerProfile = state.partnerProfile.copy(verificationStatus = "Verified"),
                snackbarMessage = "Partner ${state.partnerProfile.name} verified!"
            )
        }
    }

    fun resolveFraudAlert(alertId: String) {
        _uiState.update { state ->
            state.copy(
                fraudAlerts = state.fraudAlerts.map { if (it.id == alertId) it.copy(resolved = true) else it },
                snackbarMessage = "Fraud alert $alertId marked resolved."
            )
        }
    }

    fun resolveComplaint(complaintId: String) {
        _uiState.update { state ->
            state.copy(
                complaints = state.complaints.map { if (it.id == complaintId) it.copy(status = "Resolved") else it },
                snackbarMessage = "Complaint $complaintId resolved & refund issued."
            )
        }
    }

    fun addAdminCoupon(code: String, flatOff: Int, minOrder: Int) {
        val clean = code.trim().uppercase()
        if (clean.isBlank() || flatOff <= 0) return
        val coupon = CouponItem(clean, "Flat ₹$flatOff Off", "Admin promo valid above ₹$minOrder", flatOff, minOrder, "All Services")
        _uiState.update { state ->
            state.copy(coupons = listOf(coupon) + state.coupons, snackbarMessage = "Coupon $clean published!")
        }
    }

    // ==================== GEMINI FIREBASE AI LOGIC ====================

    fun sendAiChatQuery(prompt: String) {
        val clean = prompt.trim()
        if (clean.isBlank()) return
        val userEntry = AiChatEntry("u_${System.currentTimeMillis()}", clean, isUser = true)
        val botId = "b_${System.currentTimeMillis() + 1}"
        val placeholderBot = AiChatEntry(botId, "", isUser = false, isStreaming = true)

        _uiState.update {
            it.copy(
                aiChatMessages = it.aiChatMessages + userEntry + placeholderBot,
                aiChatLoading = true,
                aiChatError = null
            )
        }

        val state = _uiState.value
        val contextSummary = "City=${state.profile.currentCity}, Wallet=₹${state.walletBalance}, " +
            "CartItems=${state.totalCartItemsCount}, LiveOrder=${state.liveOrder?.title ?: "None"}, " +
            "Surge=${state.adminConfig.surgeMultiplier}x, ActiveRole=${state.activeRole.title}"

        viewModelScope.launch {
            try {
                aiService.streamSupportReply(clean, contextSummary).collect { partial ->
                    _uiState.update { s ->
                        s.copy(
                            aiChatMessages = s.aiChatMessages.map {
                                if (it.id == botId) it.copy(text = partial, isStreaming = true) else it
                            }
                        )
                    }
                }
                _uiState.update { s ->
                    s.copy(
                        aiChatLoading = false,
                        aiChatMessages = s.aiChatMessages.map {
                            if (it.id == botId) it.copy(isStreaming = false) else it
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errMsg = e.localizedMessage ?: "Failed to reach Gemini AI service."
                _uiState.update { s ->
                    s.copy(
                        aiChatLoading = false,
                        aiChatError = errMsg,
                        aiChatMessages = s.aiChatMessages.filterNot { it.id == botId && it.text.isBlank() }
                    )
                }
            }
        }
    }

    fun loadAiSmartRecommendations() {
        val state = _uiState.value
        val history = state.orders.take(3).joinToString("; ") { "${it.serviceType.label}: ${it.title}" }
        viewModelScope.launch {
            _uiState.update { it.copy(aiRecommendationsState = AiFetchState.Loading) }
            try {
                val items = aiService.fetchSmartRecommendations(state.profile.currentCity, history)
                _uiState.update { it.copy(aiRecommendationsState = AiFetchState.Success(items)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(aiRecommendationsState = AiFetchState.Error(e.localizedMessage ?: "Could not fetch AI recommendations."))
                }
            }
        }
    }

    fun loadAiOperationalInsights() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(aiOpsInsightState = AiFetchState.Loading) }
            try {
                val insight = aiService.fetchOperationalInsights(
                    city = state.profile.currentCity,
                    activeOrderCount = state.orders.count { it.isLive } + state.restaurantTickets.size + state.shopTickets.size,
                    activeDriverCount = state.partnerJobs.size + 18,
                    flaggedCount = state.fraudAlerts.count { !it.resolved }
                )
                _uiState.update { it.copy(aiOpsInsightState = AiFetchState.Success(insight)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(aiOpsInsightState = AiFetchState.Error(e.localizedMessage ?: "Could not run AI ops analysis."))
                }
            }
        }
    }
}
