package com.example.fit5046a3a4.screens

// Compose & Material
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

// ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel

// App logic
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.CartItemEntity
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory
import com.example.fit5046a3a4.viewmodel.UserViewModel
import com.example.fit5046a3a4.data.UserInitializer

// 时间工具
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack: () -> Unit,
    onPay: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(db.cartDao()))
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.userState.collectAsState()
    val cloudCredit by userViewModel.cloudCredit.collectAsState()

    LaunchedEffect(user?.email) {
        user?.email?.let { email ->
            userViewModel.fetchUserCredits(email)
        }
    }

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
                Text(
                    text = "Monash Dollars: \$${"%.2f".format(cloudCredit)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        val currentUser = user
                        if (currentUser != null && cloudCredit >= total) {
                            val newCredit = cloudCredit - total
                            val orderId = "ORDER-${SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(Date())}"

                            // ✅ 更新用户余额并上传订单
                            UserInitializer.updateUserCredits(
                                email = currentUser.email,
                                newCredit = newCredit,
                                onSuccess = {
                                    UserInitializer.uploadOrderToFirebase(
                                        orderId = orderId,
                                        user = currentUser,
                                        items = items,
                                        total = total,
                                        onSuccess = {
                                            cartViewModel.clear()
                                            onPay()
                                        },
                                        onFailure = { e ->
                                            println("❌ Order upload failed: ${e.message}")
                                        }
                                    )
                                },
                                onFailure = { e ->
                                    println("❌ Failed to update credits: ${e.message}")
                                }
                            )
                        } else {
                            println("⚠️ Insufficient balance or user is null")
                        }
                    },
                    enabled = cloudCredit >= total,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm to Pay")
                }
            }
        }
    }
}
