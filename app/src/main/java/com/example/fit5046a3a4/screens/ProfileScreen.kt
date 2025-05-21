package com.example.fit5046a3a4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fit5046a3a4.components.BottomBar
import com.example.fit5046a3a4.components.WithBackground
import com.example.fit5046a3a4.navigation.Screen
import com.example.fit5046a3a4.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.example.fit5046a3a4.worker.UploadToFirebaseWorker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    WithBackground {
        val userViewModel: UserViewModel = hiltViewModel()
        val user by userViewModel.userState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val currentUser = user
        val context = LocalContext.current   // ← 放在 ProfileScreen 顶部


        var isEditing by remember { mutableStateOf(false) }
        var username by remember { mutableStateOf(currentUser?.username ?: "") }

        LaunchedEffect(currentUser) {
            currentUser?.let {
                username = it.username
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
                } else {
                    Text("Username: ${currentUser?.username ?: ""}", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: ${currentUser?.email ?: ""}", style = MaterialTheme.typography.bodyLarge)
                    Text("Monash Points: ${user?.points ?: 0}", style = MaterialTheme.typography.bodyLarge)
                    Text("💵 Monash Dollars: \$${user?.dollars ?: "0.00"}", style = MaterialTheme.typography.bodyLarge)

                }

                Spacer(modifier = Modifier.height(24.dp))

                // ============== 新增：同步按钮 ==============
                Button(
                    onClick = {
                        // 立即排一个 One-Time WorkRequest，同步到云端
                        val uploadNow = OneTimeWorkRequestBuilder<UploadToFirebaseWorker>()
                            .setConstraints(
                                Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                            )
                            .build()
                        WorkManager.getInstance(context)
                            .enqueueUniqueWork(
                                "manual_upload_now",
                                ExistingWorkPolicy.REPLACE,
                                uploadNow
                            )

                        // 给出反馈
                        scope.launch {
                            snackbarHostState.showSnackbar("⏫ Sync started…")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sync to Cloud")
                }


                Button(
                    onClick = {
                        if (isEditing && currentUser != null) {
                            scope.launch {
                                userViewModel.updateUser(
                                    currentUser.copy(username = username)
                                )
                                snackbarHostState.showSnackbar("✅ Username updated!")
                            }
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditing) "Save Username" else "Edit Username")
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
