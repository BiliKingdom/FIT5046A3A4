package com.example.fit5046a3a4.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fit5046a3a4.components.WithBackground

// ✅ 改名后的本地数据类
data class PaymentItem(
    val name: String,
    val quantity: Int,
    val unitPrice: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    items: List<PaymentItem> = listOf(
        PaymentItem("Hamburger", 1, 12.99),
        PaymentItem("Coke", 2, 2.50)
    ),
    onBack: () -> Unit = {},
    onPay: () -> Unit = {}
) {
    WithBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Payment", style = MaterialTheme.typography.titleLarge)
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                containerColor = Color.Transparent
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Order Summary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        items.forEach { item ->
                            val total = item.unitPrice * item.quantity
                            Text(
                                "- ${item.name} x${item.quantity}: $${"%.2f".format(total)}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        val totalAmount = items.sumOf { it.unitPrice * it.quantity }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Total Amount: $${"%.2f".format(totalAmount)}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ProceedButton(onClick = onPay)
                }
            }
        }
    }
}

@Composable
fun ProceedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Proceed to Pay",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
