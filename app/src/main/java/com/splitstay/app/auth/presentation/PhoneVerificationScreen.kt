
package com.splitstay.app.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhoneVerificationScreen(
    mobileNumber: String,
    onVerifyOtp: (String) -> Unit = {},
    onResendOtp: () -> Unit = {},
    onChangeNumber: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val blue = Color(0xFF2878B8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Smartphone,
            contentDescription = "Phone verification",
            tint = blue,
            modifier = Modifier.size(82.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify Your Mobile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "We've sent a verification code to",
            color = Color(0xFF64748B)
        )

        Text(
            text = "+91 $mobileNumber",
            fontWeight = FontWeight.SemiBold,
            color = blue,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all(Char::isDigit)) {
                    otp = it
                    errorMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter 6-digit OTP") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (otp.length == 6) {
                    onVerifyOtp(otp)
                } else {
                    errorMessage = "Please enter a valid 6-digit OTP."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            )
        ) {
            Text("Verify Mobile Number")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onResendOtp) {
            Text("Resend OTP", color = blue)
        }

        TextButton(onClick = onChangeNumber) {
            Text("Change Mobile Number", color = Color(0xFF64748B))
        }
    }
}
