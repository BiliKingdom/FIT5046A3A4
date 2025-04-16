package com.example.fit5046a3a4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a3a4.screens.*
import android.os.Build
import androidx.annotation.RequiresApi

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Order : Screen("order")
    object Profile : Screen("profile")
    object OrderHistory : Screen("order_history")
    object Menu : Screen("menu")
    object Cart : Screen("cart")
    object Payment : Screen("payment")
    object Search : Screen("search")
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


            composable(Screen.Menu.route) {
                MenuScreen(
                    navController = navController,
                    onBack = { navController.navigateUp() }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    navController = navController
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

        }
    }
}
