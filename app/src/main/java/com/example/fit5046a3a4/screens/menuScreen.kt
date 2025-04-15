@file:JvmName("ProductDetailScreenKt")

package com.example.fit5046a3a4.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.fit5046a3a4.R
import androidx.navigation.NavHostController
import com.example.fit5046a3a4.navigation.Screen
import androidx.compose.material.icons.filled.ShoppingCart


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    val menuData = listOf(
        MenuCategory(
            "Main", listOf(
                MenuItem("Hamburger", "$12.99", R.drawable.burrito),
                MenuItem("SuperRice", "$9.99", R.drawable.taco)
            )
        ),
        MenuCategory(
            "Side", listOf(
                MenuItem("Chips", "$3.50", R.drawable.chips),
                MenuItem("Soup", "$4.00", R.drawable.soup)
            )
        ),
        MenuCategory(
            "Drink", listOf(
                MenuItem("Coke", "$2.50", R.drawable.coke),
                MenuItem("Coffee", "$2.80", R.drawable.coffee)
            )
        ),
        MenuCategory(
            "Dessert", listOf(
                MenuItem("Cake", "$5.99", R.drawable.churros),
                MenuItem("Ice Cream", "$4.50", R.drawable.icecream)
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Guzman y Gomez",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Map",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Cart.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(bottom = 4.dp)
                    ) {
                        PickupInfoCard()
                        CategoryTabBar()
                    }
                }

                menuData.forEach { category ->
                    item {
                        Text(
                            text = category.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(category.items) { item ->
                        MenuItemRow(item)
                    }
                }
            }
        }
    }
}
@Composable
fun MenuItemRow(item: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = item.name, fontWeight = FontWeight.Bold)
                Text(text = item.price, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun PickupInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        border = BorderStroke(2.dp, Color.Yellow),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Monash University 127.2 m",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Pickup time",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pickup time: 8:30 – 6:30 pm",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CategoryTabBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("MAINS", "SIDES", "DRINK", "DESSERT").forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}



data class MenuItem(
    val name: String,
    val price: String,
    val imageRes: Int
)

data class MenuCategory(
    val name: String,
    val items: List<MenuItem>
)

