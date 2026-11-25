
package com.splitstay.app.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AuthBlue = Color(0xFF2878B8)
private val AuthBackground = Color(0xFFF8FAFC)
private val AuthText = Color(0xFF334155)

@Composable
fun StudentAuthScreen(
    onSubmit: (
        fullName: String,
        email: String,
        mobile: String,
        password: String,
        isRegister: Boolean
    ) -> Unit = { _, _, _, _, _ -> },
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var isRegister by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SplitStay",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AuthBlue
        )

        Text(
            text = "A Place to Stay, A Place to Belong.",
            color = Color(0xFF64748B),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (isRegister) "Create Student Account" else "Welcome Back!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = AuthText
        )

        Text(
            text = if (isRegister)
                "Join SplitStay and find your perfect place."
            else
                "Login to continue to your account.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (isRegister) {
                    AuthField("Full Name", fullName) {
                        fullName = it
                    }
                }

                AuthField("Email Address", email) {
                    email = it
                }

                if (isRegister) {
                    AuthField("Mobile Number", mobile) {
                        mobile = it
                    }
                }

                AuthField(
                    label = "Password",
                    value = password,
                    isPassword = true
                ) {
                    password = it
                }

                if (isRegister) {
                    AuthField(
                        label = "Confirm Password",
                        value = confirmPassword,
                        isPassword = true
                    ) {
                        confirmPassword = it
                    }
                }

                validationError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = {
                        validationError = when {
                            isRegister && fullName.isBlank() ->
                                "Please enter your full name."

                            email.isBlank() ->
                                "Please enter your email address."

                            isRegister && mobile.isBlank() ->
                                "Please enter your mobile number."

                            password.isBlank() ->
                                "Please enter your password."

                            isRegister && password.length < 6 ->
                                "Password must contain at least 6 characters."

                            isRegister && password != confirmPassword ->
                                "Passwords do not match."

                            else -> null
                        }

                        if (validationError == null) {
                            onSubmit(
                                fullName.trim(),
                                email.trim(),
                                mobile.trim(),
                                password,
                                isRegister
                            )
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuthBlue
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (isRegister) "Create Account" else "Login",
                            fontSize = 16.sp
                        )
                    }
                }

                TextButton(
                    onClick = {
                        isRegister = !isRegister
                        validationError = null
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isRegister)
                            "Already have an account? Login"
                        else
                            "New to SplitStay? Create Account",
                        color = AuthBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your next home is just a step away.",
            color = Color(0xFF64748B),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword)
            PasswordVisualTransformation()
        else
            androidx.compose.ui.text.input.VisualTransformation.None
    )
}
