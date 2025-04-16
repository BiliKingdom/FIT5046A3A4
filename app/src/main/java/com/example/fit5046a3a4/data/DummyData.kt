package com.example.fit5046a3a4.data

import com.example.fit5046a3a4.R

data class Order(
    val id: String,
    val date: String,
    val time: String,
    val amount: String,
    val summary: String
)

data class Promotion(
    val id: String,
    val imageRes: Int
)

data class Restaurant(
    val name: String,
    val distanceKm: Double,
    val address: String
)

object DummyData {
    val recentOrders = listOf(
        Order("2628", "11 April 2025", "4:37 PM", "$6.70", "Ground Beef Burrito (Mild)"),
        Order("2627", "10 April 2025", "1:05 PM", "$12.00", "Chicken Bowl + Drink"),
        Order("2626", "9 April 2025", "12:12 PM", "$8.90", "Veggie Taco Combo"),
        Order("2625", "8 April 2025", "6:00 PM", "$15.00", "Family Meal"),
        Order("2624", "7 April 2025", "11:30 AM", "$5.00", "Coffee")
    )

    val promotions = listOf(
        Promotion("promo1", R.drawable.promo1),
        Promotion("promo2", R.drawable.promo2),
        Promotion("promo3", R.drawable.promo3)
    )

    val restaurants = listOf(
        Restaurant("Campus Central Café", 0.1, "Building 10, Campus Central, Clayton Campus"),
        Restaurant("LTB Café", 0.3, "Learning & Teaching Building (LTB), Ground Floor"),
        Restaurant("The Grind Espresso Bar", 0.4, "Building 12, West Wing, Clayton Campus"),
        Restaurant("Holt Café", 0.5, "Holt Building, Level 1, Clayton Campus"),
        Restaurant("Food Court Clayton", 0.6, "Building C2, Clayton Campus"),
        Restaurant("Asian Express", 0.7, "Building 18, East Wing, Clayton Campus")
    )
}
