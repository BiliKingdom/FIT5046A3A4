@file:JvmName("ProductDetailScreenKt")

package com.example.fit5046a3a4.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory
import com.example.fit5046a3a4.viewmodel.MenuViewModel
import com.example.fit5046a3a4.viewmodel.MenuViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    restaurantId: Long,
    onBack: () -> Unit = {},
    onGoToCart: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)

    // ① 拿到 CartViewModel，用于 addToCart
    val cartViewModel: CartViewModel = viewModel<CartViewModel>(
        factory = CartViewModelFactory(db.cartDao())
    )
    // ② 拿到 MenuViewModel，用于加载菜单
    val menuViewModel: MenuViewModel = viewModel<MenuViewModel>(
        factory = MenuViewModelFactory(db.foodDao(), db.restaurantDao())
    )
    // ② 收集菜单数据
    // 只在第一次 Composition 或 restaurantId 变化时调用一次
    val menuFlow = remember(restaurantId) {
        menuViewModel.loadMenuByRestaurant(restaurantId)
    }
    val menuData by menuFlow.collectAsState(initial = emptyList())

    val nameFlow = remember(restaurantId) {
        menuViewModel.getRestaurantName(restaurantId)
    }
    val restaurantName by nameFlow.collectAsState(initial = "")

    val listState = rememberLazyListState()
    val selectedCategory = remember { mutableStateOf(menuData.firstOrNull()?.name?.uppercase() ?: "") }
    val coroutineScope = rememberCoroutineScope()

    val categoryIndexMap = remember(menuData) {
        mutableMapOf<String, Int>().apply {
            var index = 1
            menuData.forEach { category ->
                put(category.name.uppercase(), index)
                index += category.items.size + 1
            }
        }
    }

    WithBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = restaurantName, style = MaterialTheme.typography.titleLarge)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onGoToCart,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                PickupInfoCard()

                Surface(
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.background
                ) {
                    CategoryTabBar(
                        categories = menuData.map { it.name.uppercase() },
                        selectedCategory = selectedCategory.value,
                        onCategorySelected = { category ->
                            selectedCategory.value = category
                            categoryIndexMap[category]?.let { targetIndex ->
                                coroutineScope.launch {
                                    listState.animateScrollToItem(targetIndex)
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    state = listState,
                    contentPadding = innerPadding,
                    modifier = Modifier.fillMaxSize()
                )  {
                    menuData.forEach { category ->
                        item {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(category.items) { menuItem ->
                            // ④ 这里把 onAdd 回调传给 MenuItemRow
                            MenuItemRow(
                                item  = menuItem,
                                onAdd = { cartViewModel.addToCart(menuItem) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTabBar(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { label ->
                val isSelected = label == selectedCategory
                Button(
                    onClick = { onCategorySelected(label) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    ),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
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
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Monash University 127.2 m",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pickup time: 8:30 – 6:30 pm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = { /* View map action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("View Map")
            }
        }
    }
}

@Composable
fun MenuItemRow(
    item: MenuItem,
    onAdd: () -> Unit     // 新增一个回调参数
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
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
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onAdd) {   // 调用传入的回调
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
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
