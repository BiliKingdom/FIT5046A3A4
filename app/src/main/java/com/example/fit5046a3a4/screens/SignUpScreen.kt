package com.example.fit5046a3a4.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a3a4.components.*
import com.example.fit5046a3a4.data.UserEntity
import com.example.fit5046a3a4.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    val userViewModel: UserViewModel = viewModel()

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

    fun validateInputs(): Boolean {
        isUsernameError = username.isBlank()
        isEmailError = email.isBlank() || !email.contains("@")
        isPasswordError = password.length < 8
        isConfirmPasswordError = password != confirmPassword
        return !isUsernameError && !isEmailError && !isPasswordError && !isConfirmPasswordError
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
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
                    errorMessage = if (isPasswordError) "Too short" else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle"
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
                                contentDescription = "Toggle"
                            )
                        }
                    }
                )

                Spacer(Modifier.height(24.dp))

                GradientButton(
                    text = "Sign Up",
                    onClick = {
                        if (validateInputs()) {
                            userViewModel.addUser(
                                UserEntity(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                            )
                            onSignUpComplete()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = validateInputs()
                )

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Log In")
                }
            }
        }
    }
}
