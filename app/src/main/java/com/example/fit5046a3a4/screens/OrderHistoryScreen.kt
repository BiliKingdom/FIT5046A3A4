package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.DummyData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.viewmodel.OrderHistoryViewModel
import com.example.fit5046a3a4.data.FirestoreOrder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController) {
    val viewModel: OrderHistoryViewModel = viewModel()
    val orders = viewModel.orders

    WithBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Your Order History",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You haven't placed any orders yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(orders) { order ->
                        val date = order.timestamp.toDate()
                        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

                        val summary = order.items.joinToString { item ->
                            val name = item["name"] as? String ?: ""
                            val qty = (item["quantity"] as? Long)?.toInt() ?: 0
                            "$name x$qty"
                        }

                        Card(
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Order #${order.orderId}", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$dateStr  |  $timeStr", style = MaterialTheme.typography.bodyMedium)
                                Text("Items: $summary", style = MaterialTheme.typography.bodyMedium)
                                Text("Total: $${"%.2f".format(order.total)}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary))
                            }
                        }
                    }
                }
            }
        }
    }
}
