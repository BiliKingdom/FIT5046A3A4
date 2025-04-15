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
import com.example.fit5046a3a4.data.DummyData
import com.example.fit5046a3a4.navigation.BottomNavItem
import com.example.fit5046a3a4.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    // 用户在搜索框输入的内容
    var query by remember { mutableStateOf("") }
    // 是否已经点击 Filter（执行过搜索）
    var hasFiltered by remember { mutableStateOf(false) }

    // 只有在点击“Filter”后才进行搜索匹配，否则为空列表或显示全部
    // 如果你想默认显示全部，可以改成 `val filteredList = ... ?: restaurants`
    val filteredList = remember(query, hasFiltered) {
        if (!hasFiltered) emptyList()  // 没执行 Filter前, 不显示任何结果
        else {
            val restaurants = DummyData.restaurants
            restaurants.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }
    }

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
                    // 主页
                    IconButton(onClick = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("main") { inclusive = false }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 顶部搜索 + Filter 按钮
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
                Button(
                    onClick = {
                        hasFiltered = true
                    }
                ) {
                    Text("Filter")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 根据过滤结果显示
            if (!hasFiltered) {
                // 尚未点击Filter，不显示任何结果
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No search has been conducted.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else if (filteredList.isEmpty()) {
                // 已经搜索，但没有结果
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No matching results were found.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                // 有匹配结果，显示
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
                                // 右侧的 "Order here" 按钮
                                Button(
                                    onClick = {
                                        navController.navigate(Screen.Menu.route)
                                    }
                                ) {
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
