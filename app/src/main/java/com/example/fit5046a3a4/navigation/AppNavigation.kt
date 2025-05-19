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

    // ✅ 添加 Map 页面
    object Map : Screen("map")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // 登录页
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

        // 注册页
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

        // 主导航区域，包含 BottomNav 页
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

            // 菜单页（传入餐厅 ID）
            composable(
                route = Screen.Menu.route,
                arguments = listOf(navArgument("restaurantId") { type = NavType.LongType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments!!.getLong("restaurantId")
                MenuScreen(
                    //navController = navController,
                    restaurantId = restaurantId,
                    onBack = { navController.navigateUp() },
                    onGoToCart = { navController.navigate(Screen.Cart.createRoute(restaurantId)) }
                )
            }

            // 购物车页（传入餐厅 ID）
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

            // 支付页面
            composable(Screen.Payment.route) {
                PaymentScreen(
                    onBack = { navController.navigateUp() },
                    onPay = {
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                )
            }

            // ✅ 地图页面（MapScreen）
            composable(Screen.Map.route) {
                MapScreen(navController = navController)
            }
        }
    }
}
