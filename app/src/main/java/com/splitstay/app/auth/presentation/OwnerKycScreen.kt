
package com.splitstay.app.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OwnerKycScreen(
    onSubmitKyc: () -> Unit = {}
) {
    var ownerName by remember { mutableStateOf("") }
    var propertyName by remember { mutableStateOf("") }
    var propertyAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var identityNumber by remember { mutableStateOf("") }

    val blue = Color(0xFF2878B8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Owner Verification",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )

        Text(
            text = "Complete your property and identity details to request account approval.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        KycField("Owner Full Name", ownerName) { ownerName = it }
        KycField("PG / Hostel Name", propertyName) { propertyName = it }
        KycField("Complete Property Address", propertyAddress) {
            propertyAddress = it
        }
        KycField("City", city) { city = it }
        KycField("Identity Document Number", identityNumber) {
            identityNumber = it
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Identity document upload",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        OutlinedButton(
            onClick = {
                // Secure document picker will be connected later.
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Upload Identity Document")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your documents will be stored privately and reviewed by the SplitStay admin team.",
            color = Color(0xFF64748B),
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmitKyc,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            )
        ) {
            Text("Submit for Approval")
        }
    }
}

@Composable
private fun KycField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}
