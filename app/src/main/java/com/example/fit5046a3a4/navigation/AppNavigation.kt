package com.example.fit5046a3a4.navigation

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

    object Product : Screen("product/{restaurantId}") {
        fun createRoute(restaurantId: Long) = "product/$restaurantId"
    }

    // ✅ 使用 String 参数传递经纬度
    object Map : Screen("map/{lat}/{lng}") {
        fun createRoute(lat: Double, lng: Double) = "map/${lat}/${lng}"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

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

            composable(
                route = Screen.Menu.route,
                arguments = listOf(navArgument("restaurantId") { type = NavType.LongType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments!!.getLong("restaurantId")
                MenuScreen(
                    restaurantId = restaurantId,
                    onBack = { navController.navigateUp() },
                    onGoToCart = { navController.navigate(Screen.Cart.createRoute(restaurantId)) },
                    onViewMap = { lat, lng ->
                        navController.navigate(Screen.Map.createRoute(lat, lng))
                    }
                )
            }

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

            composable(Screen.Payment.route) {
                PaymentScreen(
                    onBack = { navController.navigateUp() },
                    onPay = {
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                )
            }

            // ✅ MapScreen 接收经纬度参数
            composable(
                route = Screen.Map.route,
                arguments = listOf(
                    navArgument("lat") { type = NavType.StringType },
                    navArgument("lng") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: -37.9105
                val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull() ?: 145.1340
                MapScreen(navController = navController, lat = lat, lng = lng)
            }
        }
    }
}
