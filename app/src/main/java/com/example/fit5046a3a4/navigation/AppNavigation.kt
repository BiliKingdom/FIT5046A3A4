package com.example.fit5046a3a4.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fit5046a3a4.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Order : Screen("order")
    object Profile : Screen("profile")
    object OrderHistory : Screen("order_history")


    object Menu : Screen("menu/{restaurantId}") {
        fun createRoute(restaurantId: Long) = "menu/$restaurantId"
    }

    object Cart : Screen("cart/{restaurantId}") {
        fun createRoute(restaurantId: Long) = "cart/$restaurantId"
    }

    object Payment : Screen("payment")
    object Search : Screen("search")

    object Product : Screen("product/{name}/{price}/{imageRes}/{description}") {
        fun createRoute(name: String, price: String, imageRes: Int, description: String): String {
            return "product/${Uri.encode(name)}/${Uri.encode(price)}/$imageRes/${Uri.encode(description)}"
        }
    }

    // ✅ 地图界面：支持传递纬度、经度、餐厅名称、地址
    object Map : Screen("map/{lat}/{lng}/{name}/{address}") {
        fun createRoute(lat: Double, lng: Double, name: String, address: String): String {
            return "map/$lat/$lng/${Uri.encode(name)}/${Uri.encode(address)}"
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // 登录
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onNavigateToHome = {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 注册
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.navigateUp() },
                onSignUpComplete = {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 主导航区域（BottomNav）
        navigation(startDestination = BottomNavItem.Home.route, route = "main") {

            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomNavItem.Order.route) {
                OrderScreen(navController = navController)
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(navController = navController)
            }

            composable(Screen.OrderHistory.route) {
                OrderHistoryScreen(navController = navController)
            }

            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }

            // 菜单页
            composable(
                route = Screen.Menu.route,
                arguments = listOf(navArgument("restaurantId") { type = NavType.LongType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments!!.getLong("restaurantId")
                MenuScreen(
                    navController = navController,
                    restaurantId = restaurantId,
                    onBack = { navController.navigateUp() },
                    onGoToCart = {
                        navController.navigate(Screen.Cart.createRoute(restaurantId))
                    },
                    onViewMap = { lat, lng, name, address ->
                        navController.navigate(Screen.Map.createRoute(lat, lng, name, address))
                    }
                )
            }

            composable(
                route = Screen.Product.route,
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType },
                    navArgument("imageRes") { type = NavType.IntType },
                    navArgument("description") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val price = backStackEntry.arguments?.getString("price") ?: ""
                val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: 0
                val description = backStackEntry.arguments?.getString("description") ?: ""

                ProductScreen(
                    navController = navController,
                    name = name,
                    price = price,
                    imageRes = imageRes,
                    description = description
                )
            }

            // 购物车页
            composable(
                route = Screen.Cart.route,
                arguments = listOf(navArgument("restaurantId") { type = NavType.LongType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments!!.getLong("restaurantId")
                CartScreen(
                    navController = navController,
                    restaurantId = restaurantId
                )
            }

            // 支付页
            composable(Screen.Payment.route) {
                PaymentScreen(
                    onBack = { navController.navigateUp() },
                    onPay = {
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                )
            }

            // ✅ 地图页（含经纬度、名称、地址）
            composable(
                route = Screen.Map.route,
                arguments = listOf(
                    navArgument("lat") { type = NavType.StringType },
                    navArgument("lng") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("address") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: -37.9105
                val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull() ?: 145.1340
                val name = backStackEntry.arguments?.getString("name") ?: "Restaurant"
                val address = backStackEntry.arguments?.getString("address") ?: "Unknown Address"

                MapScreen(
                    navController = navController,
                    lat = lat,
                    lng = lng,
                    name = name,
                    address = address
                )
            }
        }
    }
}
