package com.example.fit5046a3a4.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.CartItemEntity
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack: () -> Unit,
    onPay: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(db.cartDao())
    )

    val items by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val total = items.sumOf { it.price * it.quantity }
    WithBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirm Your Order") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Your Order:", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))

                items.forEach { item: CartItemEntity ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.name,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(item.name, modifier = Modifier.weight(1f))
                        Text("x${item.quantity}")
                        Spacer(Modifier.width(8.dp))
                        Text("$${"%.2f".format(item.price * item.quantity)}")
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { onPay() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to pay")
                }
            }
        }
    }
}
