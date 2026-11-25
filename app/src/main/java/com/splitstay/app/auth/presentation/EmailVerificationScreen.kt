
package com.splitstay.app.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailVerificationScreen(
    email: String,
    isVerified: Boolean = false,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onResendEmail: () -> Unit = {},
    onCheckVerification: () -> Unit = {},
    onContinue: () -> Unit = {}
) {
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
            imageVector = Icons.Default.MarkEmailRead,
            contentDescription = "Email verification",
            tint = blue,
            modifier = Modifier.size(88.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify Your Email",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "We've sent a verification link to:",
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Text(
            text = email,
            fontWeight = FontWeight.SemiBold,
            color = blue,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please open your inbox and click the verification link to verify your email address.",
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (isVerified) {
            Text(
                text = "Your email has been verified successfully!",
                color = Color(0xFF15803D),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinue,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue
                )
            ) {
                Text("Continue")
            }
        } else {
            Button(
                onClick = onCheckVerification,
                enabled = !isLoading,
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
                    Text("I've Verified My Email")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onResendEmail,
                enabled = !isLoading
            ) {
                Text(
                    "Resend Verification Email",
                    color = blue
                )
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
    }
}
