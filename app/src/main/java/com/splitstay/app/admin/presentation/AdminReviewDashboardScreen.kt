
package com.splitstay.app.admin.presentation

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
import com.splitstay.app.kyc.data.KycApplication

private val AdminBlue = Color(0xFF2878B8)
private val AdminBackground = Color(0xFFF8FAFC)
private val AdminText = Color(0xFF334155)
private val AdminSecondary = Color(0xFF64748B)

@Composable
fun AdminReviewDashboardScreen(
    application: KycApplication?,
    onApprove: () -> Unit,
    onReject: (String) -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectionReason by remember { mutableStateOf("") }

    val status = application?.status
        ?.trim()
        ?.uppercase()
        ?: "NO_APPLICATION"

    val hasApplication = application != null && status != "DRAFT"

    val isPending = status == "PENDING_REVIEW"
    val isApproved = status == "APPROVED"
    val isRejected = status == "REJECTED"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Outlined.AdminPanelSettings,
                contentDescription = null,
                tint = AdminBlue,
                modifier = Modifier.size(34.dp)
            )

            Column {
                Text(
                    "Admin Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdminText
                )
                Text(
                    "Owner verification management",
                    fontSize = 13.sp,
                    color = AdminSecondary
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminStatCard(
                "Pending",
                if (isPending) "01" else "00",
                Color(0xFFF59E0B),
                Modifier.weight(1f)
            )
            AdminStatCard(
                "Approved",
                if (isApproved) "01" else "00",
                Color(0xFF10B981),
                Modifier.weight(1f)
            )
            AdminStatCard(
                "Rejected",
                if (isRejected) "01" else "00",
                Color(0xFFDC2626),
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(26.dp))

        Text(
            "Owner Applications",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = AdminText
        )

        Spacer(Modifier.height(14.dp))

        if (!hasApplication) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Inbox,
                        contentDescription = null,
                        tint = AdminSecondary,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "No applications available",
                        fontWeight = FontWeight.SemiBold,
                        color = AdminText
                    )

                    Text(
                        "Submit an owner KYC application first.",
                        color = AdminSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            val item = application!!

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        item.propertyName.ifBlank { "Property Application" },
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdminText
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        item.ownerName,
                        fontSize = 13.sp,
                        color = AdminSecondary
                    )

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        color = when (status) {
                            "APPROVED" -> Color(0xFFDCFCE7)
                            "REJECTED" -> Color(0xFFFFE4E1)
                            else -> Color(0xFFFEF3C7)
                        },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            status.replace("_", " "),
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (status) {
                                "APPROVED" -> Color(0xFF15803D)
                                "REJECTED" -> Color(0xFFB91C1C)
                                else -> Color(0xFFB45309)
                            }
                        )
                    }

                    // Review actions appear near the top of the card.
                    if (isPending) {
                        Spacer(Modifier.height(20.dp))

                        Text(
                            "Review this application",
                            fontWeight = FontWeight.Bold,
                            color = AdminText,
                            fontSize = 16.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = onApprove,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF16A34A)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Approve Application")
                        }

                        OutlinedButton(
                            onClick = {
                                rejectionReason = ""
                                showRejectDialog = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Cancel,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Reject Application")
                        }
                    } else {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            when (status) {
                                "APPROVED" -> "This demo application is approved."
                                "REJECTED" -> "Rejection reason: ${item.rejectionReason}"
                                else -> "Current status: $status. Review actions are available only for pending applications."
                            },
                            color = AdminSecondary,
                            fontSize = 13.sp
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE2E8F0)
                    )

                    Text(
                        "Application Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdminText
                    )

                    Spacer(Modifier.height(14.dp))

                    AdminDetail("Owner Name", item.ownerName)
                    AdminDetail("Property Name", item.propertyName)
                    AdminDetail("Property Type", item.propertyType)
                    AdminDetail("Address", item.propertyAddress)
                    AdminDetail("City", item.city)
                    AdminDetail("PIN Code", item.pincode)
                    AdminDetail("Identity Type", item.identityType)

                    AdminDetail(
                        "Identity Document",
                        if (item.documentSelected) "Selected locally"
                        else "Not selected"
                    )

                    AdminDetail(
                        "Property Photos",
                        "${item.propertyPhotoCount} selected locally"
                    )

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        color = Color(0xFFEAF4FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "Demo review only: actual identity documents and property photos have not been uploaded.",
                            modifier = Modifier.padding(12.dp),
                            color = AdminText,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Demo administration only. This dashboard has no real admin authentication or production approval permissions.",
            color = AdminSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(20.dp))
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = {
                showRejectDialog = false
            },
            title = {
                Text("Reject Application")
            },
            text = {
                Column {
                    Text("Enter a reason for rejecting this demo application.")

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rejectionReason,
                        onValueChange = {
                            rejectionReason = it
                        },
                        label = {
                            Text("Rejection reason")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = rejectionReason.isNotBlank(),
                    onClick = {
                        onReject(rejectionReason.trim())
                        rejectionReason = ""
                        showRejectDialog = false
                    }
                ) {
                    Text("Confirm Rejection")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRejectDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                count,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                title,
                fontSize = 11.sp,
                color = AdminSecondary
            )
        }
    }
}

@Composable
private fun AdminDetail(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 11.dp)
    ) {
        Text(
            label,
            color = AdminSecondary,
            fontSize = 11.sp
        )

        Text(
            value.ifBlank { "Not provided" },
            color = AdminText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
