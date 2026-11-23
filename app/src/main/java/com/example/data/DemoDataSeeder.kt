package com.example.data

object DemoDataSeeder {

    val popularLocations = listOf(
        "Indiranagar 100ft Rd, Bengaluru",
        "Koramangala 4th Block, Bengaluru",
        "HSR Layout Sector 2, Bengaluru",
        "Whitefield ITPL Main Rd, Bengaluru",
        "MG Road Metro Station, Bengaluru",
        "Kempegowda Int'l Airport, Bengaluru",
        "Bandra West Linking Rd, Mumbai",
        "Connaught Place Inner Circle, Delhi"
    )

    val initialProfile = CustomerProfile(
        name = "Aarav Sharma",
        phone = "+91 98765 43210",
        email = "aarav.sharma@skypulse.demo",
        avatarInitials = "AS",
        currentCity = "Bengaluru",
        currentLocation = "Indiranagar 100ft Rd, Bengaluru",
        referralCode = "SKY-AARAV50",
        referralEarned = 350,
        loyaltyPoints = 1280
    )

    val initialAddresses = listOf(
        SavedAddress("addr_1", "Home", "402 Skyview Residency, 12th Main Indiranagar, Bengaluru", "Home", true),
        SavedAddress("addr_2", "Work", "WeWork Galaxy, Residency Road, Bengaluru", "Work"),
        SavedAddress("addr_3", "Parents Home", "18 Lotus Enclave, Koramangala 4th Block, Bengaluru", "Family")
    )

    val initialFamilyMembers = listOf(
        FamilyMember("fam_1", "Meera Sharma", "Mother", "+91 98220 11223", "18 Lotus Enclave, Koramangala", 4000),
        FamilyMember("fam_2", "Rohan Sharma", "Brother", "+91 98220 44556", "Christ University Gate 2, Dairy Circle", 2500)
    )

    val initialBusinessProfile = BusinessProfile(
        companyName = "NovaCraft Logistics Pvt. Ltd.",
        gstNumber = "29AABCN4920K1Z5",
        monthlyCreditLimit = 150000,
        usedCredit = 42800,
        activeBulkShipments = 6
    )

    val rideVehicles = listOf(
        RideVehicleOption("bike", "Bike Taxi", "Bike", "Beat traffic fast • Helmet included", 25, 8, 2, 1, "FASTEST"),
        RideVehicleOption("auto", "Sky Auto", "Auto", "Zero haggle meter • Doorstep pickup", 35, 13, 3, 3, "POPULAR"),
        RideVehicleOption("mini", "Mini Cab", "Cab", "AC Hatchback • Daily comfort", 55, 16, 4, 4, "VALUE"),
        RideVehicleOption("sedan", "Prime Sedan", "Cab", "Extra legroom • Top-rated captains", 75, 19, 5, 4, "COMFORT"),
        RideVehicleOption("suv", "Sky SUV", "Cab", "Spacious 6-seater • Airport & family", 110, 24, 6, 6, "XL")
    )

    val sampleDrivers = listOf(
        DriverInfo("Vikram Patel", 4.9, 1842, "TVS Apache RTR 160 (Sky Edition)", "KA-01-EQ-4821", "+91 99001 23456"),
        DriverInfo("Suresh Gowda", 4.8, 2410, "Bajaj RE Compact CNG", "KA-03-AD-9012", "+91 99001 55678"),
        DriverInfo("Imran Khan", 4.95, 3190, "Maruti Dzire Tour S AC", "KA-05-MJ-7734", "+91 99001 88901"),
        DriverInfo("Deepak Verma", 4.85, 980, "Toyota Innova Crysta", "KA-51-ZX-2240", "+91 99001 66789")
    )

    val foodCategories = listOf(
        "All", "Pizza", "Burger", "Biryani", "Chinese", "South Indian", "North Indian", "Desserts", "Beverages"
    )

    val restaurants = listOf(
        Restaurant("rest_1", "OvenCraft Woodfire Pizza", "Italian, Artisan Pizza, Pasta", "Pizza", 4.7, 24, 149, "50% OFF up to ₹120", 1.8, isPopular = true, isFavourite = true),
        Restaurant("rest_2", "Royal Nizam Biryani House", "Hyderabadi, Mughlai, Kebabs", "Biryani", 4.8, 28, 199, "FREE Gulab Jamun", 2.3, isPopular = true, isFavourite = true),
        Restaurant("rest_3", "Smash & Melt Burgers", "Gourmet Burgers, Shakes, Fries", "Burger", 4.6, 19, 129, "Buy 1 Get 1 Classic", 1.2, isPopular = true),
        Restaurant("rest_4", "Wok Dragon Street", "Indo-Chinese, Dimsum, Bowls", "Chinese", 4.5, 22, 159, "Flat ₹75 OFF", 1.9),
        Restaurant("rest_5", "Udupi Heritage Tiffins", "South Indian, Filter Coffee", "South Indian", 4.9, 16, 99, "20% OFF Morning Combo", 0.9, isPopular = true),
        Restaurant("rest_6", "Tandoor Courtyard", "North Indian, Curries, Naan", "North Indian", 4.6, 30, 199, "Flat ₹100 OFF above ₹399", 2.6),
        Restaurant("rest_7", "Pink Velvet Patisserie", "Cakes, Brownies, Gelato", "Desserts", 4.8, 18, 120, "Free Delivery", 1.4, isFavourite = true),
        Restaurant("rest_8", "Cloud9 Boba & Brew", "Iced Coffee, Boba Tea, Smoothies", "Beverages", 4.7, 15, 110, "Combo at ₹179", 1.1)
    )

    val foodMenuItems = listOf(
        FoodMenuItem("f_1", "rest_1", "Truffle Burrata Margherita", "San Marzano tomato, fresh burrata & basil oil", "Pizza", 329, true, 4.8),
        FoodMenuItem("f_2", "rest_1", "Smoked Peri-Peri Chicken Pizza", "Char-grilled chicken, jalapenos, molten mozzarella", "Pizza", 389, false, 4.7),
        FoodMenuItem("f_3", "rest_1", "Garlic Herb Pull-Apart Bread", "Warm sourdough knots with roasted garlic butter", "Pizza", 179, true, 4.6),
        FoodMenuItem("f_4", "rest_2", "Hyderabadi Dum Chicken Biryani", "Long-grain saffron basmati cooked slow with tender chicken", "Biryani", 319, false, 4.9),
        FoodMenuItem("f_5", "rest_2", "Subz Zafrani Paneer Biryani", "Aromatic spiced vegetables & malai paneer with raita", "Biryani", 269, true, 4.7),
        FoodMenuItem("f_6", "rest_2", "Galouti Kebab Platter", "Melt-in-mouth Lucknowi kebabs served with ulte tawe ka paratha", "Biryani", 299, false, 4.8),
        FoodMenuItem("f_7", "rest_3", "Double Truffle Smash Burger", "Crispy double patty, aged cheddar, caramelized onions", "Burger", 279, false, 4.7),
        FoodMenuItem("f_8", "rest_3", "Crispy Korean Paneer Burger", "Gochujang glazed paneer fillet, pickled slaw, brioche bun", "Burger", 239, true, 4.6),
        FoodMenuItem("f_9", "rest_4", "Chilli Garlic Hakka Noodles", "Wok-tossed noodles with crisp bell peppers & burnt garlic", "Chinese", 219, true, 4.5),
        FoodMenuItem("f_10", "rest_4", "Kung Pao Chicken Bowl", "Spicy peanut stir-fry over fragrant jasmine rice", "Chinese", 289, false, 4.6),
        FoodMenuItem("f_11", "rest_5", "Ghee Podi Masala Dosa", "Crisp golden dosa roasted in pure desi ghee with gun powder", "South Indian", 149, true, 4.9),
        FoodMenuItem("f_12", "rest_5", "Kumbakonam Filter Coffee", "Authentic brass-davara strong filter kaapi", "Beverages", 79, true, 4.9),
        FoodMenuItem("f_13", "rest_6", "Dal Makhani & Butter Naan Combo", "24-hour slow simmered black lentils with two garlic naans", "North Indian", 289, true, 4.8),
        FoodMenuItem("f_14", "rest_7", "Belgian Dark Chocolate Lava Cake", "Warm molten 70% dark chocolate ganache center", "Desserts", 189, true, 4.9),
        FoodMenuItem("f_15", "rest_7", "Strawberry Pink Cheesecake Jar", "Silky cream cheese layered with fresh Mahabaleshwar berries", "Desserts", 219, true, 4.8),
        FoodMenuItem("f_16", "rest_8", "Brown Sugar Boba Iced Latte", "Cold brew espresso with chewy tapioca pearls & oat milk", "Beverages", 199, true, 4.7)
    )

    val groceryCategories = listOf(
        "All", "Fruits", "Vegetables", "Milk & Dairy", "Bakery", "Snacks", "Beverages", "Personal Care", "Household Essentials"
    )

    val groceryProducts = listOf(
        GroceryProduct("g_1", "FreshMart Express", "Organic Royal Gala Apples", "Fruits", "1 kg (6 pcs)", 189, 220, 42, 4.8),
        GroceryProduct("g_2", "FreshMart Express", "Hass Avocados (Ripe)", "Fruits", "2 pcs", 219, 260, 18, 4.7, isWishlisted = true),
        GroceryProduct("g_3", "FreshMart Express", "Hydroponic Baby Spinach", "Vegetables", "250 g", 69, 85, 34, 4.8),
        GroceryProduct("g_4", "FreshMart Express", "Farm Fresh Vine Tomatoes", "Vegetables", "1 kg", 48, 60, 85, 4.6),
        GroceryProduct("g_5", "DairyPure Hub", "A2 Gir Cow Fresh Milk", "Milk & Dairy", "1 L Glass Bottle", 110, 125, 29, 4.9, isWishlisted = true),
        GroceryProduct("g_6", "DairyPure Hub", "Greek Blueberry Yogurt", "Milk & Dairy", "400 g Tub", 145, 165, 22, 4.7),
        GroceryProduct("g_7", "Artisan Bakehouse", "Multigrain Sourdough Loaf", "Bakery", "450 g", 135, 150, 15, 4.8),
        GroceryProduct("g_8", "FreshMart Express", "Himalayan Pink Salt Kettle Chips", "Snacks", "150 g", 95, 110, 50, 4.6),
        GroceryProduct("g_9", "FreshMart Express", "Cold-Pressed Valencia Orange Juice", "Beverages", "500 ml", 129, 149, 26, 4.8),
        GroceryProduct("g_10", "CarePlus Mart", "Hydrating Aloe Face Cleanser", "Personal Care", "150 ml", 249, 299, 19, 4.7),
        GroceryProduct("g_11", "CarePlus Mart", "Eco Citrus Dishwash Liquid", "Household Essentials", "1 L Refill", 199, 240, 40, 4.8),
        GroceryProduct("g_12", "CarePlus Mart", "Bamboo Kitchen Paper Towels", "Household Essentials", "4 Rolls", 229, 279, 31, 4.6)
    )

    val parcelCategories = listOf(
        "Documents", "Clothes", "Electronics", "Gifts", "Business Packages", "Other Permitted Goods"
    )

    val transportVehicles = listOf(
        RideVehicleOption("tr_2w", "Two Wheeler", "2W", "Up to 20 kg • Urgent small items", 49, 10, 5, 1, "EXPRESS"),
        RideVehicleOption("tr_3w", "Three Wheeler", "3W", "Up to 500 kg • Appliances & cartons", 160, 22, 8, 1, "POPULAR"),
        RideVehicleOption("tr_ace", "Tata Ace", "Mini", "Up to 750 kg • 1BHK shifting & retail", 260, 28, 12, 2, "BEST VALUE"),
        RideVehicleOption("tr_pickup", "Pickup 8ft", "Pickup", "Up to 1200 kg • Furniture & hardware", 380, 34, 15, 2, "HEAVY"),
        RideVehicleOption("tr_mini", "Mini Truck 14ft", "Mini Truck", "Up to 3500 kg • Full home & B2B cargo", 690, 46, 20, 2, "B2B"),
        RideVehicleOption("tr_large", "Large Truck 19ft", "Large Truck", "Up to 7000 kg • Warehouse & industrial", 1290, 62, 30, 2, "ENTERPRISE")
    )

    val initialOrders = listOf(
        OrderRecord(
            id = "SP-FD-8841",
            serviceType = ServiceType.FOOD,
            title = "OvenCraft Woodfire Pizza",
            subtitle = "1x Truffle Burrata Margherita, 1x Garlic Bread",
            amount = 498,
            status = "On the way",
            statusStep = 4,
            totalSteps = 6,
            timestamp = "Today, 8:42 PM",
            paymentMethod = "SkyPulse Wallet",
            etaText = "Arriving in 9 mins",
            deliveryOtp = "4821",
            isLive = true
        ),
        OrderRecord(
            id = "SP-RD-7720",
            serviceType = ServiceType.RIDE,
            title = "Bike Taxi • Indiranagar → MG Road",
            subtitle = "Captain Vikram Patel • KA-01-EQ-4821",
            amount = 74,
            status = "Completed",
            statusStep = 4,
            totalSteps = 4,
            timestamp = "Yesterday, 6:15 PM",
            paymentMethod = "UPI",
            etaText = "Completed in 14 mins",
            isLive = false
        ),
        OrderRecord(
            id = "SP-GR-6612",
            serviceType = ServiceType.GROCERY,
            title = "FreshMart Express + DairyPure",
            subtitle = "A2 Cow Milk, Gala Apples, Hass Avocados",
            amount = 518,
            status = "Delivered",
            statusStep = 4,
            totalSteps = 4,
            timestamp = "Nov 20, 9:10 AM",
            paymentMethod = "Card •••• 4092",
            etaText = "Delivered in 15 mins",
            isLive = false
        ),
        OrderRecord(
            id = "SP-PR-5509",
            serviceType = ServiceType.PARCEL,
            title = "Express Parcel • Documents",
            subtitle = "Indiranagar → HSR Layout (Receiver: Neha)",
            amount = 95,
            status = "Delivered",
            statusStep = 5,
            totalSteps = 5,
            timestamp = "Nov 18, 2:30 PM",
            paymentMethod = "SkyPulse Wallet",
            etaText = "Signed Proof of Delivery",
            pickupOtp = "3190",
            deliveryOtp = "7742",
            isLive = false
        ),
        OrderRecord(
            id = "SP-TR-4401",
            serviceType = ServiceType.TRANSPORT,
            title = "Tata Ace • Office Chairs & Desk",
            subtitle = "Koramangala → Whitefield • 2 Helpers",
            amount = 920,
            status = "Completed",
            statusStep = 4,
            totalSteps = 4,
            timestamp = "Nov 15, 11:00 AM",
            paymentMethod = "Business Credit",
            etaText = "Delivered safely",
            isLive = false
        )
    )

    val initialTransactions = listOf(
        WalletTransaction("tx_1", "Added via UPI", "Top-up", 1500, true, "Today, 7:10 PM", "Success"),
        WalletTransaction("tx_2", "OvenCraft Woodfire Pizza", "Food Order", 498, false, "Today, 8:42 PM", "Paid"),
        WalletTransaction("tx_3", "Promo Cashback (SKYPULSE50)", "Promo Credit", 75, true, "Yesterday, 6:20 PM", "Credited"),
        WalletTransaction("tx_4", "Grocery Item Weight Refund", "Refund", 42, true, "Nov 20, 10:05 AM", "Refunded"),
        WalletTransaction("tx_5", "Express Parcel Booking", "Parcel", 95, false, "Nov 18, 2:30 PM", "Paid")
    )

    val initialCoupons = listOf(
        CouponItem("SKYPULSE50", "Flat ₹50 Off", "Valid on Food & Grocery carts above ₹249", 50, 249, "Food & Grocery"),
        CouponItem("FIRSTFREE", "Flat ₹80 Off", "Mega savings on Multi-Service Cart above ₹399", 80, 399, "All Services"),
        CouponItem("RIDEFAST", "₹25 Ride Cash", "Instant discount on Bike Taxi & Auto bookings", 25, 50, "Rides"),
        CouponItem("PORTERPRO", "₹150 Off Transport", "For Tata Ace, Pickup & Mini Truck bookings", 150, 499, "Transport")
    )

    val initialReviews = listOf(
        ReviewItem("rev_1", ServiceType.FOOD, "OvenCraft Woodfire Pizza", 5, "Piping hot burrata pizza arrived in 21 mins!", "Nov 21"),
        ReviewItem("rev_2", ServiceType.RIDE, "Captain Vikram Patel (Bike Taxi)", 5, "Smooth safe ride, extra helmet was super clean.", "Nov 19"),
        ReviewItem("rev_3", ServiceType.GROCERY, "FreshMart Express", 4, "Avocados were perfectly ripe and neatly packed.", "Nov 17")
    )

    val initialNotifications = listOf(
        NotificationItem("n_1", "Order SP-FD-8841 is on the way!", "Your delivery partner is 9 mins away with your pizza.", "2m ago", false),
        NotificationItem("n_2", "₹75 Promo Credit Unlocked", "Use code SKYPULSE50 on your next Multi-Service Cart.", "1h ago", false),
        NotificationItem("n_3", "Family Account Active", "Meera Sharma was added with ₹4,000 monthly allowance.", "Yesterday", true)
    )

    val initialPartnerProfile = PartnerKycProfile(
        name = "Vikram Patel",
        mobile = "+91 99001 23456",
        category = PartnerCategory.BIKE_TAXI,
        aadhaarNumber = "XXXX-XXXX-4821",
        panNumber = "ABCDE1234F",
        drivingLicence = "KA01-20210048291",
        vehicleRc = "KA-01-EQ-4821",
        vehicleInsurance = "INS-2026-88412",
        bankAccount = "HDFC •••• 7892",
        documentsUploaded = true,
        verificationStatus = "Verified",
        isOnline = true,
        todayEarnings = 1480,
        completedToday = 11,
        rating = 4.9,
        incentiveTarget = 15,
        incentiveBonus = 350
    )

    val initialPartnerJobs = listOf(
        PartnerJobRequest("JOB-901", PartnerCategory.BIKE_TAXI, "Ananya R.", "+91 98111 22334", "Indiranagar Metro", "Domlur Flyover", 3.8, 68, "2910", "5512", PartnerJobStatus.PENDING, "Bike Taxi Express Ride"),
        PartnerJobRequest("JOB-902", PartnerCategory.AUTO_CAB, "Karthik M.", "+91 98111 44556", "Koramangala Sony Signal", "MG Road", 6.2, 145, "4108", "9021", PartnerJobStatus.PENDING, "Prime Sedan Airport Connect"),
        PartnerJobRequest("JOB-903", PartnerCategory.FOOD_DELIVERY, "Priya S.", "+91 98111 66778", "OvenCraft Pizza, 100ft Rd", "HSR Sector 2", 4.5, 85, "6321", "8841", PartnerJobStatus.PENDING, "2x Woodfire Pizzas (Hot Bag)"),
        PartnerJobRequest("JOB-904", PartnerCategory.GROCERY_DELIVERY, "Rahul D.", "+91 98111 88990", "FreshMart Hub, Indiranagar", "HAL 2nd Stage", 2.1, 62, "1940", "3309", PartnerJobStatus.PENDING, "6 Daily Essential Items (2 Bags)"),
        PartnerJobRequest("JOB-905", PartnerCategory.PARCEL_DELIVERY, "Sneha K.", "+91 98111 10203", "WeWork Galaxy", "Whitefield EPIP", 11.4, 165, "7721", "4490", PartnerJobStatus.PENDING, "Sealed Legal Documents (0.5 kg)"),
        PartnerJobRequest("JOB-906", PartnerCategory.GOODS_TRANSPORT, "NovaCraft B2B", "+91 98111 30405", "Peenya Warehouse 4", "Electronic City Phase 1", 19.5, 840, "5190", "8012", PartnerJobStatus.PENDING, "Tata Ace • 12 Retail Cartons (420 kg)")
    )

    val initialRestaurantTickets = listOf(
        MerchantOrderTicket("RO-301", "Aarav Sharma", "1x Truffle Burrata Margherita, 1x Garlic Bread", 498, 18, "Preparing", "8:42 PM", "Ramesh K."),
        MerchantOrderTicket("RO-302", "Divya Menon", "2x Smoked Peri-Peri Pizza", 778, 22, "Incoming", "8:49 PM"),
        MerchantOrderTicket("RO-303", "Siddharth V.", "1x Margherita, 1x Lava Cake", 518, 15, "Ready", "8:30 PM", "Surya P.")
    )

    val initialShopTickets = listOf(
        MerchantOrderTicket("SO-401", "Neha Verma", "2x A2 Cow Milk, 1kg Gala Apples, Baby Spinach", 478, 10, "Incoming", "8:47 PM"),
        MerchantOrderTicket("SO-402", "Arjun Nair", "Hass Avocados, Sourdough Loaf, Orange Juice", 483, 12, "Packing", "8:39 PM", "Kiran B.")
    )

    val initialAdminConfig = AdminPlatformConfig(
        commissionPercent = 14.5,
        baseDeliveryFee = 29,
        serviceCharge = 12,
        surgeMultiplier = 1.1,
        dynamicPricingEnabled = true,
        activeCities = mapOf(
            "Bengaluru" to true,
            "Mumbai" to true,
            "Delhi NCR" to true,
            "Hyderabad" to true,
            "Pune" to false
        ),
        bannerHeadline = "SkyPulse SuperFest: Flat ₹50 OFF on Multi-Service Cart!"
    )

    val initialFraudAlerts = listOf(
        FraudAlert("FR-101", "SP-RD-9912", "4 rapid ride cancellations from same device fingerprint in 6 mins", "High"),
        FraudAlert("FR-102", "SP-FD-9884", "Coupon stacking anomaly detected on promo code FIRSTFREE", "Medium")
    )

    val initialComplaints = listOf(
        SupportComplaint("CMP-51", "Riya Kapoor", "Late food delivery by 18 mins — requested partial credit", "Open", 60),
        SupportComplaint("CMP-52", "Mohit Jain", "Damaged milk carton in grocery bag — instant refund", "Resolved", 110)
    )
}
