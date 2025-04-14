package com.example.fit5046a3a4.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Order : BottomNavItem("order", "Order", Icons.Default.Star)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
