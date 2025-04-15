package com.example.fit5046a3a4.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046a3a4.R
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.navigation.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    var orderType by remember { mutableStateOf("Dine In") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Your Cart")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Order.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Order"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 商品项
            CartItemRow("Hamburger", 1, R.drawable.burrito, "$12.99")
            CartItemRow("Coke", 2, R.drawable.coke, "$2.50")

            // 总价
            Text(
                text = "Summary: $17.99",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.End)
            )

            // 选择用餐方式
            Text("Choose your order type:", style = MaterialTheme.typography.titleMedium)
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
                        Text(option)
                    }
                }
            }

            // 如果是 Pick Up，显示日期和时间选择
            if (orderType == "Pick Up") {
                OutlinedTextField(
                    value = selectedDate.format(DateTimeFormatter.ISO_DATE),
                    onValueChange = {},
                    label = { Text("Pick Up Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Pick date"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Time:")
                    DropdownMenuSelector("Hour", (0..23).toList(), selectedHour) { selectedHour = it }
                    DropdownMenuSelector("Minute", listOf(0, 15, 30, 45), selectedMinute) { selectedMinute = it }
                }

                Text(
                    text = "Pickup at: ${selectedDate} ${"%02d".format(selectedHour)}:${"%02d".format(selectedMinute)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(12.dp))

            // 支付按钮
            Button(
                onClick = { navController.navigate(Screen.Payment.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Pay with Google Pay")
            }
        }

        // 日期选择弹窗
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = selectedDate
                            .atStartOfDay()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    ),
                    showModeToggle = false
                )
            }
        }
    }
}

@Composable
fun CartItemRow(item: String, quantity: Int, imageRes: Int, price: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Text(item, fontWeight = FontWeight.Bold)
                Text("x$quantity", style = MaterialTheme.typography.bodyMedium)
            }
            Text(price, color = MaterialTheme.colorScheme.primary)
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
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: ${"%02d".format(selected)}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { value ->
                DropdownMenuItem(
                    text = { Text("%02d".format(value)) },
                    onClick = {
                        onSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
