package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046a3a4.data.DummyData
import com.example.fit5046a3a4.navigation.BottomNavItem
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.components.WithBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var hasFiltered by remember { mutableStateOf(false) }

    val filteredList = remember(query, hasFiltered) {
        if (!hasFiltered) emptyList()
        else {
            val restaurants = DummyData.restaurants
            restaurants.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }
    }

    WithBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search Restaurants", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(BottomNavItem.Home.route) {
                                popUpTo("main") { inclusive = false }
                            }
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Search") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { hasFiltered = true }) {
                        Text("Filter")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!hasFiltered) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No search has been conducted.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No matching results were found.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredList) { restaurant ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = MaterialTheme.shapes.large
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = restaurant.name,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            "📍 ${restaurant.address}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "🕑 Distance: ${restaurant.distanceKm} km",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Button(onClick = {
                                        navController.navigate(Screen.Menu.route)
                                    }) {
                                        Text("Order Here")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
