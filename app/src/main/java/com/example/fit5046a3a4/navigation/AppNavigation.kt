package com.example.fit5046a3a4.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a3a4.screens.*
import com.example.fit5046a3a4.components.BottomBar
import androidx.compose.material3.Scaffold
import androidx.navigation.navigation

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
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

        // 新增部分：LocationSelectScreen 的路由添加
        // 请确保在 Screen.kt 中已定义例如：
        // object LocationSelect : Screen("locationSelect")
        composable(Screen.Location.route) {
            LocationScreen(
                navController = navController,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // 底部导航部分（主界面）
        navigation(startDestination = BottomNavItem.Home.route, route = "main") {
            composable(BottomNavItem.Home.route) {
                MainScaffold(navController, currentScreen = BottomNavItem.Home)
            }
            composable(BottomNavItem.Order.route) {
                MainScaffold(navController, currentScreen = BottomNavItem.Order)
            }
            composable(BottomNavItem.Profile.route) {
                MainScaffold(navController, currentScreen = BottomNavItem.Profile)
            }

            composable(Screen.Form.route) {
                FormScreen(onNavigateBack = { navController.navigateUp() })
            }
        }
    }
}

@Composable
fun MainScaffold(navController: NavHostController, currentScreen: BottomNavItem) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        when (currentScreen) {
            is BottomNavItem.Home -> HomeScreen(
                navController = navController,  // 传入 navController 参数
                onNavigateToForm = {
                    navController.navigate(Screen.Form.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
            is BottomNavItem.Order -> OrderScreen()
            is BottomNavItem.Profile -> ProfileScreen()
        }
    }
}
