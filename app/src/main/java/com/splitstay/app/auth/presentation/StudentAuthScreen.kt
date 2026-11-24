
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
fun StudentAuthScreen() {
    var isRegister by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                    AuthField("Full Name", fullName) { fullName = it }
                }

                AuthField("Email Address", email) { email = it }

                if (isRegister) {
                    AuthField("Mobile Number", mobile) { mobile = it }
                }

                AuthField(
                    "Password",
                    password,
                    isPassword = true
                ) { password = it }

                if (isRegister) {
                    AuthField(
                        "Confirm Password",
                        confirmPassword,
                        isPassword = true
                    ) { confirmPassword = it }
                }

                Button(
                    onClick = {
                        // Firebase authentication will be connected later.
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuthBlue
                    )
                ) {
                    Text(
                        if (isRegister) "Create Account" else "Login",
                        fontSize = 16.sp
                    )
                }

                TextButton(
                    onClick = { isRegister = !isRegister },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (isRegister)
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
