package com.example.fit5046a3a4.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fit5046a3a4.data.FirestoreOrder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class OrderHistoryViewModel : ViewModel() {
    private val _orders = mutableStateListOf<FirestoreOrder>()
    val orders: List<FirestoreOrder> = _orders

    init {
        fetchOrdersFromFirestore()
    }

    private fun fetchOrdersFromFirestore() {
        val uid = Firebase.auth.currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("orders")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                _orders.clear()
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FirestoreOrder::class.java)
                }.forEach { order ->
                    _orders.add(order)
                }
            }
            .addOnFailureListener { e ->
                Log.e("OrderHistory", "Failed to fetch orders: ${e.message}")
            }
    }
}
