package com.example.fit5046a3a4.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit5046a3a4.components.BrandLogo
import com.example.fit5046a3a4.components.GradientButton
import com.example.fit5046a3a4.components.StyledTextField
import com.example.fit5046a3a4.data.UserEntity
import com.example.fit5046a3a4.viewmodel.UserViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var isUsernameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isConfirmPasswordError by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }

    val auth = Firebase.auth

    fun validateInputs(): Boolean {
        isUsernameError = username.isBlank()
        isEmailError = email.isBlank() || !email.contains("@")
        isPasswordError = password.length < 8
        isConfirmPasswordError = password != confirmPassword
        return !isUsernameError && !isEmailError && !isPasswordError && !isConfirmPasswordError
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandLogo(Modifier.padding(bottom = 24.dp))
                Text("Create Account", style = MaterialTheme.typography.headlineMedium)

                StyledTextField(
                    value = username,
                    onValueChange = { username = it; isUsernameError = false },
                    label = "Username",
                    isError = isUsernameError,
                    errorMessage = if (isUsernameError) "Enter username" else null,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = email,
                    onValueChange = { email = it; isEmailError = false },
                    label = "Email",
                    isError = isEmailError,
                    errorMessage = if (isEmailError) "Invalid email" else null,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = password,
                    onValueChange = { password = it; isPasswordError = false },
                    label = "Password",
                    isError = isPasswordError,
                    errorMessage = if (isPasswordError) "Too short (min 8 chars)" else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password"
                            )
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; isConfirmPasswordError = false },
                    label = "Confirm Password",
                    isError = isConfirmPasswordError,
                    errorMessage = if (isConfirmPasswordError) "Passwords do not match" else null,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle confirm password"
                            )
                        }
                    }
                )

                Spacer(Modifier.height(24.dp))

                GradientButton(
                    text = if (isSubmitting) "Registering..." else "Sign Up",
                    onClick = {
                        if (validateInputs()) {
                            isSubmitting = true
                            val displayName = username.ifBlank { email.substringBefore("@") }

                            scope.launch {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener {
                                        val newUser = UserEntity(
                                            username = displayName,
                                            email = email,
                                            password = password,
                                            dollars = 1.0,
                                            points = 0
                                        )
                                        scope.launch {
                                            val userId = userViewModel.addUserReturnId(newUser)
                                            val fullUser = newUser.copy(id = userId)

                                            // 上传到 Firebase，使用自增 ID
                                            Firebase.firestore.collection("users")
                                                .document(email)
                                                .set(
                                                    mapOf(
                                                        "id" to userId,
                                                        "username" to displayName,
                                                        "email" to email,
                                                        "dollars" to 1.0,
                                                        "points" to 0
                                                    )
                                                )
                                                .addOnSuccessListener {
                                                    userViewModel.setUser(fullUser)
                                                    isSubmitting = false
                                                    onSignUpComplete()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("SignUpScreen", "🔥 Firestore failed: ${e.message}")
                                                    isSubmitting = false
                                                }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("SignUpScreen", "Firebase signup failed: ${e.message}")
                                        isSubmitting = false
                                    }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = validateInputs() && !isSubmitting
                )

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Log In")
                }
            }
        }
    }
}
