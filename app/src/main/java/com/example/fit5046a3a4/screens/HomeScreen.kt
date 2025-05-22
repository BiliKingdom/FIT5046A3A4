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
import com.example.fit5046a3a4.data.UserInitializer
import android.util.Log
import androidx.compose.ui.text.font.FontWeight
import com.example.fit5046a3a4.R
import com.example.fit5046a3a4.data.WeatherResponse
import com.example.fit5046a3a4.data.api.fetchWeather
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.viewmodel.OrderHistoryViewModel
import com.example.fit5046a3a4.data.FirestoreOrder
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.auth.ktx.auth




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val orderViewModel: OrderHistoryViewModel = viewModel()
    val orders = orderViewModel.orders
    val user by userViewModel.userState.collectAsState()
    val cloudCredit by userViewModel.cloudCredit.collectAsState()
    val firebaseUserEmail = Firebase.auth.currentUser?.email


    LaunchedEffect(firebaseUserEmail) {
        firebaseUserEmail?.let { email ->
            userViewModel.syncUserFromFirebase(email)
            userViewModel.fetchUserCredits(email)
            orderViewModel.fetchOrdersByEmail(email)
        }
    }


    val campuses = listOf("Clayton", "Caulfield")
    val campusCoordinates = mapOf(
        "Clayton" to Pair(-37.911, 145.134),
        "Caulfield" to Pair(-37.877, 145.043)
    )
    var selectedCampus by remember { mutableStateOf("Clayton") }
    var expanded by remember { mutableStateOf(false) }
    var weatherInfo by remember { mutableStateOf<WeatherResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedCampus) {
        val (lat, lon) = campusCoordinates[selectedCampus]!!
        coroutineScope.launch {
            try {
                weatherInfo = fetchWeather(lat, lon)
            } catch (e: Exception) {
                weatherInfo = null
            }
        }
    }



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

            Text("Monash Dollars: \$${"%.2f".format(cloudCredit)}", style = MaterialTheme.typography.bodyLarge)


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
                        Image(
                            painter = painterResource(id = R.drawable.weather),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Current Weather: ${
                                    weatherInfo?.weather?.firstOrNull()?.main ?: "Loading..."
                                }",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Temperature: ${
                                    weatherInfo?.main?.temp?.let { "$it°C" } ?: "Loading..."
                                }",
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
                                    }
                                )
                            }
                        }
                    }
                }
            }

            
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
                items(orders.take(5)) { order ->
                    val date = order.timestamp.toDate()
                    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
                    val summary = order.items.joinToString { item ->
                        val name = item["name"] as? String ?: ""
                        val qty = (item["quantity"] as? Long)?.toInt() ?: 0
                        "$name x$qty"
                    }

                    Card(
                        modifier = Modifier.width(260.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order #${order.orderId}", style = MaterialTheme.typography.titleSmall)
                            Text("$dateStr | $timeStr", style = MaterialTheme.typography.bodySmall)
                            Text(summary, style = MaterialTheme.typography.bodyMedium)
                            Text("$${"%.2f".format(order.total)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                // TODO: Reorder
                            }, modifier = Modifier.fillMaxWidth()) {
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
