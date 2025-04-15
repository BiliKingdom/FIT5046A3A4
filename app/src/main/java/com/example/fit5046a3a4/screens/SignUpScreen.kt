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
import com.example.fit5046a3a4.components.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpComplete: () -> Unit
) {
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            tonalElevation = 1.dp,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp)
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandLogo(modifier = Modifier.padding(bottom = 24.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isUsernameError = false
                    },
                    label = "Username",
                    isError = isUsernameError,
                    errorMessage = if (isUsernameError) "Please enter a username" else null,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailError = false
                    },
                    label = "Email",
                    isError = isEmailError,
                    errorMessage = if (isEmailError) {
                        if (email.isBlank()) "Please enter your email" else "Please enter a valid email"
                    } else null,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordError = false
                    },
                    label = "Password",
                    isError = isPasswordError,
                    errorMessage = if (isPasswordError) "Password must be at least 8 characters" else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                PasswordStrengthIndicator(
                    strength = calculatePasswordStrength(password),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Password requirements:\n" +
                            "• At least 8 characters\n" +
                            "• Uppercase and lowercase letters\n" +
                            "• At least one digit\n" +
                            "• At least one special character",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        isConfirmPasswordError = false
                    },
                    label = "Confirm Password",
                    isError = isConfirmPasswordError,
                    errorMessage = if (isConfirmPasswordError) "Passwords do not match" else null,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Sign Up",
                    onClick = {
                        if (validateInputs()) {
                            onSignUpComplete()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = username.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank()
                )

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Already have an account? Log In",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun calculatePasswordStrength(password: String): Float {
    var strength = 0f
    if (password.length >= 8) strength += 0.25f
    if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) strength += 0.25f
    if (password.any { it.isDigit() }) strength += 0.25f
    if (password.any { !it.isLetterOrDigit() }) strength += 0.25f
    return strength
}
