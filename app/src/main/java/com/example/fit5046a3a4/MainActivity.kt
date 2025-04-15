package com.example.fit5046a3a4

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.fit5046a3a4.navigation.AppNavigation
import com.example.fit5046a3a4.ui.theme.FIT5046A3A4Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.fit5046a3a4.components.WithBackground

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FIT5046A3A4Theme {
                val systemUiController = rememberSystemUiController()
                val isDarkTheme = isSystemInDarkTheme()

                DisposableEffect(systemUiController, isDarkTheme) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isDarkTheme
                    )
                    onDispose {}
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    WithBackground {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
