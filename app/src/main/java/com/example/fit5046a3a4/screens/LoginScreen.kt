package com.example.fit5046a3a4.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.R
import com.example.fit5046a3a4.components.BrandLogo
import com.example.fit5046a3a4.data.UserEntity
import com.example.fit5046a3a4.data.UserInitializer
import com.example.fit5046a3a4.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val userViewModel: UserViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    // Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        val emailFromGoogle = auth.currentUser?.email
                        val nameFromGoogle = account.displayName ?: "GoogleUser"
                        if (emailFromGoogle != null) {
                            val user = UserEntity(
                                username = nameFromGoogle,
                                email = emailFromGoogle,
                                password = "google_user"
                            )
                            UserInitializer.initializeFirestoreUserIfNew(user)
                            userViewModel.clearUser()
                            userViewModel.syncUserFromFirebase(emailFromGoogle) {
                                onNavigateToHome()
                            }
                        }
                    } else {
                        Log.e("LoginScreen", "Firebase Auth failed: ${authResult.exception}")
                    }
                }
            } catch (e: ApiException) {
                Log.w("LoginScreen", "Google sign in failed: ${e.statusCode}")
            }
        }
    }

    // Google Sign-In client
    val googleSignInClient = remember {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("765683541411-fq762aa9oidafs85lbh9in4kgkkq8nl2.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, options)
    }

    fun validateInputs(): Boolean {
        isEmailError = email.isBlank()
        isPasswordError = password.isBlank()
        return !isEmailError && !isPasswordError
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandLogo(modifier = Modifier.padding(bottom = 32.dp))
            Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; isEmailError = false },
                label = { Text("Email") },
                isError = isEmailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; isPasswordError = false },
                label = { Text("Password") },
                isError = isPasswordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Button(
                onClick = {
                    if (validateInputs()) {
                        isEmailError = false
                        isPasswordError = false
                        isSubmitting = true

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                userViewModel.clearUser()
                                val username = email.substringBefore("@")
                                val user = UserEntity(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                                UserInitializer.initializeFirestoreUserIfNew(user)
                                userViewModel.syncUserFromFirebase(email) {
                                    onNavigateToHome()
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("LoginScreen", "Login failed: ${e.message}")
                                isEmailError = true
                                isPasswordError = true
                                isSubmitting = false
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                        val intent = googleSignInClient.signInIntent.apply {
                            putExtra("prompt", "select_account")
                        }
                        launcher.launch(intent)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.googlelogin),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google", color = Color.Black)
            }

            TextButton(onClick = onNavigateToSignUp, modifier = Modifier.padding(top = 16.dp)) {
                Text("Don't have an account? Sign Up!", color = Color.Black)
            }
        }
    }
}
