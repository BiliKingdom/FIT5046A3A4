@file:JvmName("ProductDetailScreenKt")

package com.example.fit5046a3a4.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory
import com.example.fit5046a3a4.viewmodel.MenuViewModel
import com.example.fit5046a3a4.viewmodel.MenuViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    navController: NavHostController,
    restaurantId: Long,
    onBack: () -> Unit = {},
    onGoToCart: () -> Unit,
    onViewMap: (Double, Double, String, String) -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)

    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(db.cartDao()))
    val menuViewModel: MenuViewModel = viewModel(factory = MenuViewModelFactory(db.foodDao(), db.restaurantDao()))

    val menuFlow = remember(restaurantId) {
        menuViewModel.loadMenuByRestaurant(restaurantId)
    }
    val menuData by menuFlow.collectAsState(initial = emptyList())

    val restaurantFlow = remember(restaurantId) {
        menuViewModel.getRestaurantByIdFlow(restaurantId)
    }
    val restaurantEntity by restaurantFlow.collectAsState(initial = null)

    val listState = rememberLazyListState()
    val selectedCategory = remember { mutableStateOf(menuData.firstOrNull()?.name?.uppercase() ?: "") }
    val coroutineScope = rememberCoroutineScope()

    val categoryIndexMap = remember(menuData) {
        mutableMapOf<String, Int>().apply {
            var index = 0
            menuData.forEach { category ->
                put(category.name.uppercase(), index)
                index += category.items.size + 1
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    WithBackground {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = restaurantEntity?.name ?: "Restaurant", style = MaterialTheme.typography.titleLarge)
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
                PickupInfoCard(
                    onViewMap = {
                        restaurantEntity?.let {
                            onViewMap(it.latitude, it.longitude, it.name, it.address)
                        }
                    }
                )

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

                //Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    menuData.forEach { category ->
                        item {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(category.items) { menuItem ->
                            MenuItemRow(
                                item = menuItem,
                                onAdd = {
                                    cartViewModel.addToCart(menuItem)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Successfully added a product to cart !")
                                    }
                                },
                                onClick = {
                                    navController.navigate(
                                        Screen.Product.createRoute(
                                            name = menuItem.name,
                                            price = menuItem.price,
                                            imageRes = menuItem.imageRes,
                                            description = menuItem.description
                                        )
                                    )
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun PickupInfoCard(onViewMap: () -> Unit) {
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
                Text("Monash University 127.2 m", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Pickup time: 8:30 – 6:30 pm", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = onViewMap,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("View Map")
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { label ->
                val isSelected = label == selectedCategory
                Button(
                    onClick = { onCategorySelected(label) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(label, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem, onAdd: () -> Unit,onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Text(item.name, fontWeight = FontWeight.Bold)
                Text(item.price, color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

// --- Menu Models ---

data class MenuItem(
    val name: String,
    val price: String,
    val imageRes: Int,
    val description: String
)

data class MenuCategory(
    val name: String,
    val items: List<MenuItem>
)
