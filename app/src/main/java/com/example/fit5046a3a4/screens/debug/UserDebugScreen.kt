//package com.example.fit5046a3a4.screens.debug
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.fit5046a3a4.viewmodel.UserViewModel
//import com.example.fit5046a3a4.data.UserEntity
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UserDebugScreen() {
//    val userViewModel: UserViewModel = viewModel()
//    val users by userViewModel.userDao.getAllUsers().collectAsState(initial = emptyList())
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("🧪 Debug: All Users") })
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//            items(users) { user: UserEntity ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("ID: ${user.id}", style = MaterialTheme.typography.bodyMedium)
//                        Text("Username: ${user.username}", style = MaterialTheme.typography.bodyMedium)
//                        Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
//                        Text("Password: ${user.password}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
//                    }
//                }
//            }
//        }
//    }
//}
