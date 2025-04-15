package com.example.fit5046a3a4.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight



// 数据类用于传递订单项
data class CartItem(val name: String, val quantity: Int, val unitPrice: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    items: List<CartItem> = listOf(
        CartItem("Hamburger", 1, 12.99),
        CartItem("Coke", 2, 2.50)
    ),
    onBack: () -> Unit = {},
    onPay: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Order Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                items.forEach { item ->
                    val total = item.unitPrice * item.quantity
                    Text("- ${item.name} x${item.quantity}: $${"%.2f".format(total)}")
                }
                val totalAmount = items.sumOf { it.unitPrice * it.quantity }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Total Amount: $${"%.2f".format(totalAmount)}", fontSize = 18.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.weight(1f))

            SlideToProceedButton(onComplete = onPay)
        }
    }
}

@Composable
fun SlideToProceedButton(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffset = 220f
    val isComplete = offsetX > maxOffset * 0.85f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = if (isComplete) Color(0xFF4CAF50) else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (isComplete) "✔ Proceed" else "Slide to Proceed",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        Surface(
            modifier = Modifier
                .offset(x = offsetX.dp)
                .size(48.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxOffset)
                    }
                },
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Slide",
                tint = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }

    if (isComplete) {
        LaunchedEffect(true) {
            delay(400)
            onComplete()
        }
    }
}