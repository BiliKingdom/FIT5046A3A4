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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.navigation.BottomNavItem
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.OrderViewModel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController) {
    val viewModel: OrderViewModel = viewModel()
    val restaurants by viewModel.restaurantList.collectAsState()
    val campuses = listOf("Clayton", "Caulfield")
    var selectedCampus by remember { mutableStateOf("Clayton") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCampus) {
        viewModel.loadRestaurantsForCampus(selectedCampus)
    }

    WithBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Select a Restaurant", style = MaterialTheme.typography.titleLarge) },
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
            bottomBar = { BottomBar(navController = navController) },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Current Campus:", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { expanded = true }) {
                        Text(selectedCampus)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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

                Spacer(modifier = Modifier.height(16.dp))

                if (restaurants.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No restaurants available.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(restaurants) { res ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(res.name, style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(4.dp))
                                    Text(res.address, style = MaterialTheme.typography.bodyMedium)

                                    Button(
                                        onClick = {
                                            navController.navigate(Screen.Menu.createRoute(res.id))
                                        },
                                        modifier = Modifier
                                            .padding(top = 12.dp)
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text("Order Here", style = MaterialTheme.typography.bodyMedium)
                                    }

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
