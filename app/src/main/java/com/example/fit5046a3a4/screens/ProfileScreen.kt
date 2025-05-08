package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    WithBackground {
        val userViewModel: UserViewModel = hiltViewModel()
        val user by userViewModel.userState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // ❗️明确保存 user 引用，避免 Smart Cast 报错
        val currentUser = user

        var isEditing by remember { mutableStateOf(false) }
        var username by remember { mutableStateOf(currentUser?.username ?: "") }
        var email by remember { mutableStateOf(currentUser?.email ?: "") }

        // 同步 user 变化时更新输入框值
        LaunchedEffect(currentUser) {
            currentUser?.let {
                username = it.username
                email = it.email
            }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Profile", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (isEditing) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("Username: $username", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: $email", style = MaterialTheme.typography.bodyLarge)
                    Text("Monash Points: 29", style = MaterialTheme.typography.bodyLarge)
                    Text("💵 Monash Dollars: \$54.30", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isEditing && currentUser != null) {
                            scope.launch {
                                userViewModel.updateUser(
                                    currentUser.copy(username = username, email = email)
                                )
                                snackbarHostState.showSnackbar("✅ Profile updated!")
                            }
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditing) "Save Changes" else "Edit Profile")
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            userViewModel.clearUser()
                            snackbarHostState.showSnackbar("👋 Logged out successfully!")
                        }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
