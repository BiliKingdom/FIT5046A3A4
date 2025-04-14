package com.example.fit5046a3a4.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import com.example.fit5046a3a4.R


@Composable
fun ProductScreen() {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Add to Order")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 商品图片
            Image(
                painter = painterResource(id = R.drawable.burrito), // 你自己的图片资源
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 商品名称和价格
            Text("Hamburger", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$8.20",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 简要介绍
            Text(
                text = "A delicious breakfast Hamburger made with scrambled eggs, bacon, cheese, and salsa wrapped in a warm tortilla.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 数量选择器
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                QuantitySelector()
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
        Text("Number:", modifier = Modifier.padding(end = 8.dp))

        IconButton(
            onClick = { if (quantity > 1) quantity-- },
            modifier = Modifier
                .border(1.dp, Color.Gray, CircleShape)
                .size(36.dp)
        ) {
            Text("-", style = MaterialTheme.typography.bodyLarge)
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(
            onClick = { quantity++ },
            modifier = Modifier
                .border(1.dp, Color.Gray, CircleShape)
                .size(36.dp)
        ) {
            Text("+", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

