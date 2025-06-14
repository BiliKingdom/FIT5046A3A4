package com.example.fit5046a3a4.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    restaurantId: Long,
) {
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(db.cartDao()))



    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    WithBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            var orderType by remember { mutableStateOf("Dine In") }
            var showDatePicker by remember { mutableStateOf(false) }
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            var selectedHour by remember { mutableStateOf(12) }
            var selectedMinute by remember { mutableStateOf(0) }

            val items by cartViewModel.cartItems.collectAsState(initial = emptyList())
            val total = items.sumOf { it.price * it.quantity }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Your Cart", style = MaterialTheme.typography.titleLarge) },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack(
                                    route = Screen.Menu.createRoute(restaurantId),
                                    inclusive = false
                                )
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back to Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                bottomBar = { BottomBar(navController) },
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(items) { cart ->
                        CartItemRow(
                            item = cart.name,
                            quantity = cart.quantity,
                            imageRes = cart.imageRes,
                            price = "$${"%.2f".format(cart.price)}",
                            onRemove = { cartViewModel.remove(cart) }
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Summary: $${"%.2f".format(total)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Choose your order type:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        val options = listOf("Dine In", "Pick Up")
                        Row {
                            options.forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .selectable(
                                            selected = (option == orderType),
                                            onClick = { orderType = option },
                                            role = Role.RadioButton
                                        )
                                ) {
                                    RadioButton(
                                        selected = (option == orderType),
                                        onClick = { orderType = option }
                                    )
                                    Text(text = option, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }

                        val now = java.time.LocalDateTime.now()
                        val selectedDateTime = java.time.LocalDateTime.of(selectedDate, java.time.LocalTime.of(selectedHour, selectedMinute))
                        val minValidTime = now.plusMinutes(15)
                        val isValidPickupTime = selectedDateTime.isAfter(minValidTime)


                        if (orderType == "Pick Up") {
                            OutlinedTextField(
                                value = selectedDate.format(DateTimeFormatter.ISO_DATE),
                                onValueChange = {},
                                label = { Text("Pick Up Date", style = MaterialTheme.typography.bodyLarge) },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = "Pick date")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Time:", style = MaterialTheme.typography.bodyLarge)
                                DropdownMenuSelector("Hour", (0..23).toList(), selectedHour) {
                                    selectedHour = it
                                }
                                DropdownMenuSelector("Minute", listOf(0, 15, 30, 45), selectedMinute) {
                                    selectedMinute = it
                                }
                            }

                            Text(
                                text = if (!isValidPickupTime)
                                    "⚠ Pickup time must be at least 15 minutes from now"
                                else
                                    "Pickup at: ${selectedDate} ${"%02d".format(selectedHour)}:${"%02d".format(selectedMinute)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (!isValidPickupTime) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )


                        }

                        Spacer(Modifier.height(12.dp))

                        val isButtonEnabled = items.isNotEmpty() && (orderType != "Pick Up" || isValidPickupTime)

                        Button(
                            onClick = {
                                if (!isButtonEnabled) return@Button
                                navController.navigate(Screen.Payment.route)
                            },
                            enabled = isButtonEnabled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Proceed to Pay", style = MaterialTheme.typography.labelLarge)
                        }

                    }
                }

                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate
                        .atStartOfDay()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                )


                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    selectedDate = java.time.Instant.ofEpochMilli(millis)
                                        .atZone(java.time.ZoneId.systemDefault())
                                        .toLocalDate()
                                }
                                showDatePicker = false
                            }) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState, showModeToggle = false)
                    }
                }

            }
        }
    }
}

@Composable
fun CartItemRow(item: String, quantity: Int, imageRes: Int, price: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = item,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(item, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Text("x$quantity", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = price,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
fun DropdownMenuSelector(
    label: String,
    options: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.background(Color.White)
        ) {
            Text("$label: ${"%02d".format(selected)}", style = MaterialTheme.typography.bodyLarge)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { value ->
                DropdownMenuItem(
                    text = {
                        Text("%02d".format(value), style = MaterialTheme.typography.bodyLarge)
                    },
                    onClick = {
                        onSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
