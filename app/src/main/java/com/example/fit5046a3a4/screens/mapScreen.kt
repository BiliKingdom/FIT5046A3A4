package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    lat: Double,
    lng: Double,
    name: String,
    address: String
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
            val targetPosition = LatLng(lat, lng)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(targetPosition, 15f)
            }

            // ✅ 地图部分
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = targetPosition),
                    title = name,
                    snippet = address
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
