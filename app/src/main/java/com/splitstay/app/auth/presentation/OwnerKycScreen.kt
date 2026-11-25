
package com.splitstay.app.auth.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val KycBlue = Color(0xFF2878B8)
private val KycBackground = Color(0xFFF8FAFC)
private val KycText = Color(0xFF334155)
private val KycSecondary = Color(0xFF64748B)
private val KycBorder = Color(0xFFE2E8F0)

@Composable
fun OwnerKycScreen(
    onSubmitKyc: () -> Unit = {}
) {
    var ownerName by remember { mutableStateOf("") }
    var propertyName by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("PG") }
    var propertyAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var identityType by remember { mutableStateOf("PAN Card") }
    var identityLastFour by remember { mutableStateOf("") }

    var identityDocument by remember { mutableStateOf<Uri?>(null) }
    var propertyPhotos by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var showError by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) identityDocument = uri
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        propertyPhotos = uris
    }

    val isFormValid =
        ownerName.isNotBlank() &&
                propertyName.isNotBlank() &&
                propertyAddress.isNotBlank() &&
                city.isNotBlank() &&
                pincode.length == 6 &&
                identityLastFour.length == 4 &&
                identityDocument != null &&
                propertyPhotos.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KycBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.VerifiedUser,
                contentDescription = null,
                tint = KycBlue,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = "Owner Verification",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = KycText
                )
                Text(
                    text = "Complete your verification details",
                    fontSize = 13.sp,
                    color = KycSecondary
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Surface(
            color = Color(0xFFEAF4FF),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = KycBlue
                )
                Text(
                    text = "Your owner account is pending verification. " +
                            "Complete this form to prepare your KYC application.",
                    color = KycText,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        KycSectionTitle("Personal Information", "Your basic details")

        KycField("Owner Full Name", ownerName) { ownerName = it }

        Spacer(Modifier.height(12.dp))

        KycSectionTitle("Property Information", "Tell us about your property")

        KycField("PG / Hostel Name", propertyName) {
            propertyName = it
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Property Type",
            color = KycText,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("PG", "Hostel").forEach { type ->
                FilterChip(
                    selected = propertyType == type,
                    onClick = { propertyType = type },
                    label = { Text(type) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        KycField("Complete Property Address", propertyAddress) {
            propertyAddress = it
        }

        Spacer(Modifier.height(12.dp))

        KycField("City", city) { city = it }

        Spacer(Modifier.height(12.dp))

        KycField(
            label = "6-digit PIN Code",
            value = pincode,
            onValueChange = { pincode = it.filter(Char::isDigit).take(6) }
        )

        Spacer(Modifier.height(24.dp))

        KycSectionTitle("Identity Verification", "Confirm your identity")

        Text(
            "Identity Document Type",
            color = KycText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("PAN Card", "Aadhaar").forEach { type ->
                FilterChip(
                    selected = identityType == type,
                    onClick = { identityType = type },
                    label = { Text(type) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        KycField(
            label = "Last 4 characters of document number",
            value = identityLastFour,
            onValueChange = {
                identityLastFour = it.take(4).uppercase()
            }
        )

        Text(
            "For this demo, enter only the last 4 characters. " +
                    "Do not enter your full Aadhaar or PAN number.",
            color = KycSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(14.dp))

        UploadCard(
            title = "Identity Document",
            description = "Choose a PDF or image from your device",
            selectedText = if (identityDocument != null)
                "Document selected"
            else
                "No document selected",
            icon = Icons.Outlined.Description,
            onClick = {
                documentPicker.launch(
                    arrayOf("application/pdf", "image/*")
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        KycSectionTitle("Property Photos", "Show your accommodation")

        UploadCard(
            title = "Property Images",
            description = "Select room, bathroom and common-area photos",
            selectedText = if (propertyPhotos.isNotEmpty())
                "${propertyPhotos.size} photo(s) selected"
            else
                "No photos selected",
            icon = Icons.Outlined.AddPhotoAlternate,
            onClick = {
                photoPicker.launch(arrayOf("image/*"))
            }
        )

        Spacer(Modifier.height(14.dp))

        Surface(
            color = Color(0xFFEDE9FE),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Privacy notice: This is currently a local demo. " +
                        "Files are selected on your device but are not uploaded " +
                        "to Firebase or submitted for admin review.",
                modifier = Modifier.padding(14.dp),
                color = KycText,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }

        if (showError) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Please complete all required fields and select your identity document and at least one property photo.",
                color = Color(0xFFDC2626),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    showError = false
                    showConfirmation = true
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KycBlue
            )
        ) {
            Icon(Icons.Outlined.Send, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                "Review KYC Details",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(20.dp))
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            icon = {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = KycBlue
                )
            },
            title = { Text("Details Ready") },
            text = {
                Text(
                    "Your details have passed the local form checks. " +
                            "No documents have been uploaded and no application " +
                            "has been sent to an admin yet."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmation = false
                        onSubmitKyc()
                    }
                ) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmation = false }
                ) {
                    Text("Edit Details")
                }
            }
        )
    }
}

@Composable
private fun KycSectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = KycText
        )
        Text(
            subtitle,
            fontSize = 13.sp,
            color = KycSecondary
        )
    }
    Spacer(Modifier.height(14.dp))
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
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KycBlue,
            focusedLabelColor = KycBlue
        )
    )
}

@Composable
private fun UploadCard(
    title: String,
    description: String,
    selectedText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            KycBorder
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = KycBlue,
                modifier = Modifier.size(30.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = KycText,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    description,
                    color = KycSecondary,
                    fontSize = 12.sp
                )
                Text(
                    selectedText,
                    color = KycBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                Icons.Outlined.UploadFile,
                contentDescription = "Choose file",
                tint = KycBlue
            )
        }
    }
}
