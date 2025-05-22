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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.fit5046a3a4.viewmodel.CartViewModel
import com.example.fit5046a3a4.viewmodel.CartViewModelFactory
import com.example.fit5046a3a4.data.AppDatabase
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    WithBackground {
        val userViewModel: UserViewModel = hiltViewModel()
        val user by userViewModel.userState.collectAsState()
        val cloudCredit by userViewModel.cloudCredit.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var isEditing by remember { mutableStateOf(false) }
        var username by remember { mutableStateOf(user?.username ?: "") }

        val firebaseUserEmail = Firebase.auth.currentUser?.email

        val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(AppDatabase.get(context).cartDao()))

        // 进入 ProfileScreen 时主动 fetch 云端 Monash Dollars
        LaunchedEffect(firebaseUserEmail) {
            firebaseUserEmail?.let { email ->
                userViewModel.fetchUserCredits(email)
            }
        }

        LaunchedEffect(user) {
            user?.let {
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
                    Text("Username: ${user?.username ?: ""}", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: ${user?.email ?: ""}", style = MaterialTheme.typography.bodyLarge)
                    // 👇 余额用云端同步的 cloudCredit 保证和 Home 一致
                    Text(
                        "Monash Dollars: \$${"%.2f".format(cloudCredit)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 同步到云端按钮
                Button(
                    onClick = {
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

                        scope.launch {
                            snackbarHostState.showSnackbar("⏫ Sync started…")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sync to Cloud")
                }

                // 编辑/保存用户名按钮
                Button(
                    onClick = {
                        val currentUser = user
                        if (isEditing && currentUser != null) {
                            val updatedUser = currentUser.copy(username = username)

                            userViewModel.updateUser(updatedUser) // 本地 Room 数据库
                            userViewModel.updateUserInFirebase(updatedUser) // 🔄 Firestore 云同步

                            scope.launch {
                                snackbarHostState.showSnackbar("✅ Username updated!")
                            }
                        }

                        isEditing = !isEditing
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditing) "Save Username" else "Edit Username")
                }

                // 登出按钮
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            userViewModel.clearUser()
                            cartViewModel.clear()
                            snackbarHostState.showSnackbar("Logged out successfully!")
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