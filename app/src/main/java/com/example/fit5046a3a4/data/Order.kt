package com.example.fit5046a3a4.data

import com.google.firebase.Timestamp

data class FirestoreOrder(
    val orderId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val items: List<Map<String, Any>> = emptyList(),
    val total: Double = 0.0
)