package com.example.fit5046a3a4.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.data.DummyData
import com.example.fit5046a3a4.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Welcome") })
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("HELLO, TIM 👋", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⭐ Monash Points: 29", color = MaterialTheme.colorScheme.primary)
            Text("💵 Monash Dollars: $54.30", color = MaterialTheme.colorScheme.secondary)

            Spacer(Modifier.height(24.dp))

            // Recent Orders  + View All
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🛍️ Recent Orders", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = {
                    navController.navigate(Screen.OrderHistory.route)
                }) {
                    Text("View All")
                }
            }

            // Recent Orders
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(DummyData.recentOrders) { order ->
                    Card(
                        modifier = Modifier
                            .width(260.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order #${order.id}", style = MaterialTheme.typography.titleSmall)
                            Text("📅 ${order.date} | 🕒 ${order.time}", style = MaterialTheme.typography.bodySmall)
                            Text("🍽️ ${order.summary}", style = MaterialTheme.typography.bodyMedium)
                            Text("💰 ${order.amount}", color = MaterialTheme.colorScheme.primary)

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { /* TODO: Reorder */ },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reorder")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("🎉 Promotions", style = MaterialTheme.typography.titleMedium)

            // Promotions 列表
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(DummyData.promotions) { promo ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .width(300.dp)
                            .height(150.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = promo.imageRes),
                            contentDescription = "Promotion",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
