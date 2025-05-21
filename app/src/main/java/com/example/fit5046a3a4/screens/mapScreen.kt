package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    lat: Double,
    lng: Double
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Map") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val cameraPositionState = rememberCameraPositionState {
                position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(lat, lng), 15f
                )
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = com.google.android.gms.maps.model.LatLng(lat, lng)),
                    title = "Restaurant Location",
                    snippet = "Selected Restaurant"
                )
            }
        }
    }
}
