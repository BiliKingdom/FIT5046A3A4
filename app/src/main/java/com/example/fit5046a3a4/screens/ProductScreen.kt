package com.example.fit5046a3a4.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit5046a3a4.R
import com.example.fit5046a3a4.components.WithBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen() {
    WithBackground {
        Box(modifier = Modifier.fillMaxSize()) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp, // 状态栏下方 + 微调
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )

                Text(
                    text = "Product Detail",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 24.dp, end = 24.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.burrito),
                        contentDescription = "Product",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Breakfast Burrito", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "$8.20",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A delicious breakfast burrito made with scrambled eggs, bacon, cheese, and salsa wrapped in a warm tortilla.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                QuantitySelector()
            }


            Button(
                onClick = { /* Do something */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF28B00B),
                    contentColor = Color.White
                )
            ) {
                Text("Add to Order", fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun QuantitySelector() {
    var quantity by remember { mutableStateOf(1) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Number:", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Medium)

        IconButton(
            onClick = { if (quantity > 1) quantity-- },
            modifier = Modifier
                .border(1.dp, Color.Gray, CircleShape)
                .size(36.dp)
        ) {
            Text("-", fontSize = 20.sp)
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp
        )

        IconButton(
            onClick = { quantity++ },
            modifier = Modifier
                .border(1.dp, Color.Gray, CircleShape)
                .size(36.dp)
        ) {
            Text("+", fontSize = 20.sp)
        }
    }
}
