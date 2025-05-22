package com.example.fit5046a3a4.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirestoreOrderDebugScreen() {
    val TAG = "FirestoreDebug"
    var orders by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.data }
                orders = list
                isLoading = false
                Log.d(TAG, "Orders loaded: $list")
            }
            .addOnFailureListener {
                error = it.message
                isLoading = false
                Log.e(TAG, "Error fetching orders", it)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Firestore Order Debug") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            if (isLoading) {
                Text("Loading orders from Firestore...")
            } else if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            } else if (orders.isEmpty()) {
                Text("No orders found.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(orders) { order ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Order ID: ${order["orderId"]}")
                                Text("Email: ${order["email"]}")
                                Text("Total: $${order["total"]}")
                                Text("Items: ${(order["items"] as? List<*>)?.size ?: 0} items")
                            }
                        }
                    }
                }
            }
        }
    }
}
