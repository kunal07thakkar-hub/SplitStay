
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OwnerBlue = Color(0xFF2878B8)
private val OwnerPurple = Color(0xFF8064B5)
private val OwnerBackground = Color(0xFFF8FAFC)
private val OwnerText = Color(0xFF334155)

@Composable
fun OwnerAuthScreen(
    onSubmit: (
        fullName: String,
        propertyName: String,
        email: String,
        mobile: String,
        password: String,
        isRegister: Boolean
    ) -> Unit = { _, _, _, _, _, _ -> },
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var isRegister by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var propertyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SplitStay",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = OwnerBlue
        )

        Text(
            text = "A Place to Stay, A Place to Belong.",
            color = Color(0xFF64748B),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (isRegister) "Create Owner Account" else "Welcome Back!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = OwnerText
        )

        Text(
            text = if (isRegister)
                "Manage your PG or hostel with SplitStay."
            else
                "Login to manage your property.",
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
                    OwnerAuthField("Owner Full Name", fullName) {
                        fullName = it
                    }

                    OwnerAuthField("PG / Hostel Name", propertyName) {
                        propertyName = it
                    }
                }

                OwnerAuthField("Email Address", email) {
                    email = it
                }

                if (isRegister) {
                    OwnerAuthField("Mobile Number", mobile) {
                        mobile = it
                    }
                }

                OwnerAuthField(
                    label = "Password",
                    value = password,
                    isPassword = true
                ) {
                    password = it
                }

                if (isRegister) {
                    OwnerAuthField(
                        label = "Confirm Password",
                        value = confirmPassword,
                        isPassword = true
                    ) {
                        confirmPassword = it
                    }

                    Text(
                        text = "After registration, you'll verify your email and mobile number, then submit your KYC details for admin approval.",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
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

                            isRegister && propertyName.isBlank() ->
                                "Please enter your PG or hostel name."

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
                                propertyName.trim(),
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
                        containerColor = OwnerPurple
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
                            if (isRegister) "Create Owner Account" else "Login",
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
                            "Already registered? Login"
                        else
                            "New to SplitStay? Register",
                        color = OwnerBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your property, managed better.",
            color = Color(0xFF64748B),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun OwnerAuthField(
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
            VisualTransformation.None
    )
}
