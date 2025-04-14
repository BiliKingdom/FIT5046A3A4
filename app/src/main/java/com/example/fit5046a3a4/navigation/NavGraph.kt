
package com.example.fit5046a3a4.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Form : Screen("form")
}

sealed class NavGraph(val route: String) {
    object Auth : NavGraph("auth")
    object Main : NavGraph("main")
}