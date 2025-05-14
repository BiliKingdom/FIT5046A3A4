package com.example.fit5046a3a4.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.data.DummyData
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.userState.collectAsState()

    val campuses = listOf("Clayton", "Caulfield")
    var selectedCampus by remember { mutableStateOf("Clayton") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Welcome", style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "HELLO, ${user?.username ?: "User"}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                modifier = Modifier.offset(y = (-16).dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text("Monash Points: ${user?.points ?: 0}", style = MaterialTheme.typography.bodyLarge)
            Text("Monash Dollars: \$${user?.dollars ?: "0.00"}", style = MaterialTheme.typography.bodyLarge)

            // ☁️ Weather card + Dropdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "Weather Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Rain expected in $selectedCampus", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Bring your umbrella and be careful as the road is slippery!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Current Campus: $selectedCampus")
                        IconButton(onClick = { expanded = true }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Change Location")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            campuses.forEach { campus ->
                                DropdownMenuItem(
                                    text = { Text(campus) },
                                    onClick = {
                                        selectedCampus = campus
                                        expanded = false
                                        // ⭐ 预留地图API逻辑：你可以在这里调用地图更新方法
                                        // updateMapLocation(campus)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 🧾 Recent Orders
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recent Orders", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = {
                    navController.navigate(Screen.OrderHistory.route)
                }) {
                    Text("View All", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(DummyData.recentOrders) { order ->
                    Card(
                        modifier = Modifier.width(260.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order #${order.id}", style = MaterialTheme.typography.titleSmall)
                            Text("${order.date} | ${order.time}", style = MaterialTheme.typography.bodySmall)
                            Text(order.summary, style = MaterialTheme.typography.bodyMedium)
                            Text(order.amount, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { /* TODO: Reorder */ }, modifier = Modifier.fillMaxWidth()) {
                                Text("Reorder", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("Promotions", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

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
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
