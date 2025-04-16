package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.DummyData
import com.example.fit5046a3a4.navigation.BottomNavItem
import com.example.fit5046a3a4.navigation.Screen
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController) {
    WithBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Select a Restaurant",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = {
                            navController.navigate(BottomNavItem.Home.route) {
                                popUpTo("main") { inclusive = false }
                            }
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Choose a nearby Monash restaurant to place your order:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(DummyData.restaurants) { res ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = res.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = res.address,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Distance: ${res.distanceKm} km",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Button(
                                    onClick = {
                                        navController.navigate(Screen.Menu.route)
                                    },
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "Order Here",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Map integration coming soon...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
