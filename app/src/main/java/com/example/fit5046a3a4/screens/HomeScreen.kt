package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.example.fit5046a3a4.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onNavigateToLocation: () -> Unit = {},
    onNavigateToForm: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedLocation by remember { mutableStateOf<String?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    /**
     * 当返回栈发生变化时（例如 LocationScreen -> navigateUp），
     * 自动检查 savedStateHandle 里有没有 "selectedLocation"，
     * 有就取出来更新按钮文本，然后删掉，避免重复。
     */
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            backStackEntry.savedStateHandle
                .get<String>("selectedLocation")
                ?.let { name ->
                    selectedLocation = name           // 更新按钮文本
                    backStackEntry.savedStateHandle.remove<String>("selectedLocation")
                }
        }
    }



    NavigationDrawer(
        drawerState = drawerState,
        onNavigateToForm = onNavigateToForm,
        onLogout = onLogout
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("GuzmanyGomez") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {//new info and selection func below
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pick Up Only",
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 20.sp),
                                modifier = Modifier.offset(x = 16.dp)
                            )
                            Button(
                                onClick = {
                                    // 修改5：点击按钮后通过 navController 导航到 LocationScreen
                                    navController.navigate(Screen.Location.route)
                                },
                                modifier = Modifier.padding(start = 8.dp)
                                    .height(48.dp)
                                    .width(170.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Select a location",
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                                Text(
                                    text = selectedLocation ?: "Select a location",
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                        }
                        // The above is the demarcation line for the new features.

                        Text(
                            text = "Welcome Back",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "What would you like to eat today?",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Quick Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onNavigateToForm,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Form",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Fill Form")
                    }

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "Orders",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("My Orders")
                    }
                }

                // Feature Cards Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeatureCard(
                        icon = Icons.Default.Restaurant,
                        title = "Browse Menu",
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    FeatureCard(
                        icon = Icons.Default.Favorite,
                        title = "Favorites",
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeatureCard(
                        icon = Icons.Default.LocationOn,
                        title = "Store Locations",
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    FeatureCard(
                        icon = Icons.Default.Person,
                        title = "Profile",
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationDrawer(
    drawerState: DrawerState,
    onNavigateToForm: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    label = { Text("Fill Form") },
                    selected = false,
                    onClick = onNavigateToForm
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Logout") },
                    selected = false,
                    onClick = onLogout
                )
            }
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /* TODO */ },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title)
        }
    }
}