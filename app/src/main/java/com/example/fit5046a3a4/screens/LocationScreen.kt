package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
//preview func import
import androidx.navigation.compose.rememberNavController
import com.example.fit5046a3a4.ui.theme.FIT5046A3A4Theme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    onNavigateBack: () -> Unit,  // 可选：返回按钮点击时调用
    navController: NavHostController
) {
    data class Store(val name: String, val address: String)
    var selectedStoreName by remember { mutableStateOf<String?>(null) }
    val stores = listOf(
        Store(
            name = "Glen Waverley Store",
            address = "Shop 12, 45 High Street, Glen Waverley VIC 3150, Australia"
        ),
        Store(
            name = "Burwood Mall",
            address = "Unit 8, 100 Burwood Highway, Burwood VIC 3125, Australia"
        ),
        Store(
            name = "Mulgrave Market",
            address = "Store 3, 15 Centre Road, Mulgrave VIC 3170, Australia"
        ),
        Store(
            name = "Mitcham Hub",
            address = "Shop 4, 50 Burwood Road, Mitcham VIC 3132, Australia"
        ),
        Store(
            name = "Wheelers Hill Centre",
            address = "Outlet 5, 200 Queens Road, Wheelers Hill VIC 3150, Australia"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Location") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items =stores) { store ->
                val isSelected = (store.name == selectedStoreName)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 显示门店信息：门店名和具体地址
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = store.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = store.address,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        // "Order here" 按钮，占位实现选择返回功能
                        Button(
                            onClick = {
                                // 存储选中的门店到 SavedStateHandle，以便回到 HomeScreen 后能读取
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedLocation", store.name)

                                // 仅更新本地 isSelected 状态，不立即 navigateUp()
                                selectedStoreName = store.name
                            },
                            // 如果要在视觉上区分选中，可改变按钮颜色
                            colors = if (isSelected) {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            } else {
                                ButtonDefaults.buttonColors()
                            }
                        ) {
                            Text("Order here")
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    FIT5046A3A4Theme {
        // 创建一个临时的 NavHostController 用于预览
        val navController = rememberNavController()
        LocationScreen(
            navController = navController,
            onNavigateBack = {}
        )
    }
}
