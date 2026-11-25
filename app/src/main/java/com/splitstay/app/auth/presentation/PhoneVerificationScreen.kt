
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhoneVerificationScreen(
    mobileNumber: String,
    isLoading: Boolean = false,
    isVerified: Boolean = false,
    errorMessage: String? = null,
    onVerifyOtp: (String) -> Unit = {},
    onResendOtp: () -> Unit = {},
    onChangeNumber: (String) -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var newNumber by remember { mutableStateOf("") }
    var showNumberEditor by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val blue = Color(0xFF2878B8)

    val displayNumber = mobileNumber
        .trim()
        .removePrefix("+91")

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
            text = if (isVerified) "Mobile Verified!" else "Verify Your Mobile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isVerified)
                "Your mobile number has been successfully verified."
            else
                "We've sent a verification code to",
            color = Color(0xFF64748B)
        )

        Text(
            text = "+91 $displayNumber",
            fontWeight = FontWeight.SemiBold,
            color = blue,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (isVerified) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Your phone verification is complete.",
                color = Color(0xFF64748B)
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = {
                    if (it.length <= 6 && it.all(Char::isDigit)) {
                        otp = it
                        validationError = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter 6-digit OTP") },
                singleLine = true,
                enabled = !isLoading && !showNumberEditor,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )

            validationError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (otp.length == 6) {
                        validationError = null
                        onVerifyOtp(otp)
                    } else {
                        validationError = "Please enter a valid 6-digit OTP."
                    }
                },
                enabled = !isLoading && !showNumberEditor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Verify Mobile Number")
                }
            }

            TextButton(
                onClick = onResendOtp,
                enabled = !isLoading && !showNumberEditor
            ) {
                Text("Resend OTP", color = blue)
            }

            TextButton(
                onClick = {
                    newNumber = displayNumber
                    showNumberEditor = !showNumberEditor
                    validationError = null
                },
                enabled = !isLoading
            ) {
                Text(
                    if (showNumberEditor) "Cancel"
                    else "Change Mobile Number",
                    color = Color(0xFF64748B)
                )
            }

            if (showNumberEditor) {
                OutlinedTextField(
                    value = newNumber,
                    onValueChange = {
                        if (it.length <= 10 && it.all(Char::isDigit)) {
                            newNumber = it
                            validationError = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("New 10-digit mobile number") },
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Button(
                    onClick = {
                        if (newNumber.length == 10) {
                            validationError = null
                            onChangeNumber(newNumber)
                            otp = ""
                            showNumberEditor = false
                        } else {
                            validationError =
                                "Please enter a valid 10-digit mobile number."
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue
                    )
                ) {
                    Text("Send OTP to New Number")
                }
            }
        }
    }
}