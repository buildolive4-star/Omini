package com.example.data

enum class ServiceType(val label: String) {
    RIDE("Ride"),
    FOOD("Food"),
    GROCERY("Grocery"),
    PARCEL("Parcel"),
    TRANSPORT("Transport")
}

enum class UserRole(val title: String, val badge: String, val demoCode: String) {
    CUSTOMER("Customer", "Member", "0000"),
    DRIVER_PARTNER("Driver / Delivery Partner", "Partner", "1111"),
    RESTAURANT_OWNER("Restaurant Owner", "Merchant", "2222"),
    GROCERY_OWNER("Grocery Store Owner", "Store", "3333"),
    BUSINESS_ACCOUNT("Business Dashboard", "Enterprise", "4444"),
    ADMIN("Super Admin", "Admin", "9999")
}

enum class CustomerSection(val title: String) {
    MY_RIDES("My Rides"),
    MY_FOOD_ORDERS("My Food Orders"),
    MY_GROCERY_ORDERS("My Grocery Orders"),
    MY_PARCEL_BOOKINGS("My Parcel Bookings"),
    MY_TRANSPORT_BOOKINGS("My Transport Bookings"),
    MY_PAYMENTS("My Payments"),
    MY_COUPONS("My Coupons"),
    MY_ADDRESSES("My Addresses"),
    MY_FAVOURITES("My Favourites"),
    MY_REVIEWS("My Reviews"),
    HELP_SUPPORT("Help & Support"),
    SETTINGS("Settings"),
    FAMILY_ACCOUNT("Family Account"),
    BUSINESS_DASHBOARD("Business Dashboard")
}

sealed interface AppRoute {
    data object Splash : AppRoute
    data object Home : AppRoute
    data object Services : AppRoute
    data object Orders : AppRoute
    data object Wallet : AppRoute
    data object Profile : AppRoute
    data object Ride : AppRoute
    data object Food : AppRoute
    data object Grocery : AppRoute
    data object Parcel : AppRoute
    data object Transport : AppRoute
    data object MultiCart : AppRoute
    data object AiAssistant : AppRoute
    data class CustomerDetail(val section: CustomerSection) : AppRoute
    data class RoleAuthGate(val targetRole: UserRole) : AppRoute
    data object PartnerDashboard : AppRoute
    data object PartnerRegistration : AppRoute
    data object RestaurantPanel : AppRoute
    data object ShopPanel : AppRoute
    data object AdminPanel : AppRoute
}

data class CustomerProfile(
    val name: String,
    val phone: String,
    val email: String,
    val avatarInitials: String,
    val currentCity: String,
    val currentLocation: String,
    val referralCode: String,
    val referralEarned: Int,
    val loyaltyPoints: Int
)

data class SavedAddress(
    val id: String,
    val label: String,
    val fullAddress: String,
    val tag: String,
    val isDefault: Boolean = false
)

data class FamilyMember(
    val id: String,
    val name: String,
    val relation: String,
    val phone: String,
    val savedAddress: String,
    val monthlyLimit: Int
)

data class BusinessProfile(
    val companyName: String,
    val gstNumber: String,
    val monthlyCreditLimit: Int,
    val usedCredit: Int,
    val activeBulkShipments: Int
)

data class RideVehicleOption(
    val id: String,
    val name: String,
    val category: String,
    val subtitle: String,
    val baseFare: Int,
    val perKmRate: Int,
    val etaMinutes: Int,
    val seats: Int,
    val badge: String
)

data class DriverInfo(
    val name: String,
    val rating: Double,
    val tripsCount: Int,
    val vehicleModel: String,
    val vehiclePlate: String,
    val phone: String
)

enum class RidePhase(val label: String) {
    SEARCHING("Matching Nearest Captain"),
    DRIVER_ASSIGNED("Captain Arriving"),
    OTP_VERIFIED("Ride In Progress"),
    COMPLETED("Ride Completed")
}

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String
)

data class ActiveRide(
    val id: String,
    val pickup: String,
    val drop: String,
    val distanceKm: Double,
    val vehicle: RideVehicleOption,
    val fare: Int,
    val paymentMethod: String,
    val otp: String,
    val driver: DriverInfo,
    val phase: RidePhase,
    val progress: Float,
    val chatMessages: List<ChatMessage>,
    val userRating: Int = 0,
    val feedback: String = ""
)

data class FoodMenuItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String,
    val category: String,
    val price: Int,
    val isVeg: Boolean,
    val rating: Double,
    val isAvailable: Boolean = true,
    val customizations: List<String> = listOf("Regular", "Extra Cheese +₹35", "Spicy +₹15")
)

data class Restaurant(
    val id: String,
    val name: String,
    val cuisine: String,
    val category: String,
    val rating: Double,
    val deliveryMinutes: Int,
    val minOrder: Int,
    val offerTag: String,
    val distanceKm: Double,
    val isPopular: Boolean = false,
    val isFavourite: Boolean = false
)

data class GroceryProduct(
    val id: String,
    val shopName: String,
    val name: String,
    val category: String,
    val unit: String,
    val price: Int,
    val mrp: Int,
    val stock: Int,
    val rating: Double,
    val isExpressEligible: Boolean = true,
    val isWishlisted: Boolean = false
)

data class CartItem(
    val itemId: String,
    val title: String,
    val sourceName: String,
    val serviceType: ServiceType,
    val unitPrice: Int,
    val quantity: Int,
    val customization: String = ""
)

data class OrderRecord(
    val id: String,
    val serviceType: ServiceType,
    val title: String,
    val subtitle: String,
    val amount: Int,
    val status: String,
    val statusStep: Int,
    val totalSteps: Int,
    val timestamp: String,
    val paymentMethod: String,
    val etaText: String,
    val pickupOtp: String = "",
    val deliveryOtp: String = "",
    val isRefundRequested: Boolean = false,
    val canRebook: Boolean = true,
    val isLive: Boolean = false
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Int,
    val isCredit: Boolean,
    val timestamp: String,
    val status: String
)

data class CouponItem(
    val code: String,
    val title: String,
    val description: String,
    val discountFlat: Int,
    val minOrder: Int,
    val serviceScope: String
)

data class ReviewItem(
    val id: String,
    val serviceType: ServiceType,
    val targetName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val time: String,
    val isRead: Boolean = false
)

enum class PartnerCategory(val label: String, val serviceType: ServiceType) {
    BIKE_TAXI("Bike Taxi", ServiceType.RIDE),
    AUTO_CAB("Auto / Cab", ServiceType.RIDE),
    FOOD_DELIVERY("Food Delivery", ServiceType.FOOD),
    GROCERY_DELIVERY("Grocery Delivery", ServiceType.GROCERY),
    PARCEL_DELIVERY("Parcel Delivery", ServiceType.PARCEL),
    GOODS_TRANSPORT("Goods Transport", ServiceType.TRANSPORT)
}

enum class PartnerJobStatus(val label: String) {
    PENDING("New Request"),
    ACCEPTED("Heading to Pickup"),
    PICKED_UP("On Way to Drop"),
    COMPLETED("Delivered / Completed")
}

data class PartnerJobRequest(
    val id: String,
    val category: PartnerCategory,
    val customerName: String,
    val customerPhone: String,
    val pickup: String,
    val drop: String,
    val distanceKm: Double,
    val payout: Int,
    val pickupOtp: String,
    val deliveryOtp: String,
    val status: PartnerJobStatus,
    val itemSummary: String
)

data class PartnerKycProfile(
    val name: String,
    val mobile: String,
    val category: PartnerCategory,
    val aadhaarNumber: String,
    val panNumber: String,
    val drivingLicence: String,
    val vehicleRc: String,
    val vehicleInsurance: String,
    val bankAccount: String,
    val documentsUploaded: Boolean,
    val verificationStatus: String, // Verified, Pending Review
    val isOnline: Boolean,
    val todayEarnings: Int,
    val completedToday: Int,
    val rating: Double,
    val incentiveTarget: Int,
    val incentiveBonus: Int
)

data class MerchantOrderTicket(
    val id: String,
    val customerName: String,
    val itemsSummary: String,
    val totalAmount: Int,
    val prepMinutes: Int,
    val status: String, // Incoming, Accepted, Preparing/Packing, Ready, Dispatched, Rejected
    val timestamp: String,
    val assignedDriver: String = "Unassigned"
)

data class AdminPlatformConfig(
    val commissionPercent: Double,
    val baseDeliveryFee: Int,
    val serviceCharge: Int,
    val surgeMultiplier: Double,
    val dynamicPricingEnabled: Boolean,
    val activeCities: Map<String, Boolean>,
    val bannerHeadline: String
)

data class FraudAlert(
    val id: String,
    val bookingId: String,
    val reason: String,
    val riskLevel: String,
    val resolved: Boolean = false
)

data class SupportComplaint(
    val id: String,
    val userName: String,
    val topic: String,
    val status: String,
    val refundAmount: Int = 0
)

data class AiChatEntry(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val isStreaming: Boolean = false
)
