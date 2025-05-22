package com.example.fit5046a3a4.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fit5046a3a4.data.FirestoreOrder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderHistoryViewModel : ViewModel() {
    private val _orders = mutableStateListOf<FirestoreOrder>()
    val orders: List<FirestoreOrder> = _orders

    // ✅ 调用此函数并传入当前用户邮箱，以从 Firestore 加载对应订单
    fun fetchOrdersByEmail(userEmail: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .whereEqualTo("email", userEmail)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                _orders.clear()
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FirestoreOrder::class.java)
                }.forEach { order ->
                    _orders.add(order)
                }
                Log.d("OrderHistoryVM", "Fetched ${_orders.size} orders for $userEmail")
            }
            .addOnFailureListener { e ->
                Log.e("OrderHistoryVM", "Failed to fetch orders: ${e.message}")
            }
    }
}
