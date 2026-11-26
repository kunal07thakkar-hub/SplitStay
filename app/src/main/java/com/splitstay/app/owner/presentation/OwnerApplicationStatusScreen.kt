
package com.splitstay.app.owner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.splitstay.app.kyc.data.KycApplication

private val StatusBlue = Color(0xFF2878B8)
private val StatusBackground = Color(0xFFF8FAFC)
private val StatusText = Color(0xFF334155)
private val StatusSecondary = Color(0xFF64748B)

@Composable
fun OwnerApplicationStatusScreen(
    application: KycApplication,
    onOpenAdminDemo: () -> Unit = {}
) {
    val status = application.status

    val approved = status == "APPROVED"
    val rejected = status == "REJECTED"

    val statusColor = when (status) {
        "APPROVED" -> Color(0xFF10B981)
        "REJECTED" -> Color(0xFFDC2626)
        else -> Color(0xFFF59E0B)
    }

    val statusTitle = when (status) {
        "APPROVED" -> "Application Approved"
        "REJECTED" -> "Application Rejected"
        else -> "Application Under Review"
    }

    val statusMessage = when (status) {
        "APPROVED" ->
            "Your owner verification has been marked as approved in this demo."

        "REJECTED" ->
            "Your application needs changes before it can be approved."

        else ->
            "Your application is saved locally and is waiting for the demo review stage."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StatusBackground)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Outlined.VerifiedUser,
                contentDescription = null,
                tint = StatusBlue,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    "Owner Application",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusText
                )
                Text(
                    "Verification status",
                    color = StatusSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = when (status) {
                        "APPROVED" -> Icons.Outlined.CheckCircle
                        "REJECTED" -> Icons.Outlined.Cancel
                        else -> Icons.Outlined.Schedule
                    },
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(58.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    statusTitle,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusText
                )

                Spacer(Modifier.height(8.dp))

                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = when (status) {
                            "APPROVED" -> "APPROVED"
                            "REJECTED" -> "REJECTED"
                            else -> "PENDING REVIEW"
                        },
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    statusMessage,
                    color = StatusSecondary,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Application Progress",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = StatusText
        )

        Spacer(Modifier.height(18.dp))

        val applicationCreated = status != "DRAFT"
        val reviewComplete = approved || rejected

        StatusStep(
            icon = Icons.Outlined.Description,
            title = "Application Created",
            subtitle = if (applicationCreated) {
                "Your application was saved in this demo."
            } else {
                "Your application is still a draft."
            },
            completed = applicationCreated,
            active = status == "DRAFT"
        )

        StatusStep(
            icon = Icons.Outlined.FactCheck,
            title = "Admin Verification",
            subtitle = when {
                approved -> "Demo approval completed."
                rejected -> "Demo review resulted in rejection."
                applicationCreated -> "Waiting for demo review."
                else -> "Starts after application submission."
            },
            completed = reviewComplete,
            active = applicationCreated && !reviewComplete,
            isLast = true
        )

        if (rejected && application.rejectionReason.isNotBlank()) {
            Spacer(Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE4E1)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Reason for rejection",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB91C1C)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        application.rejectionReason,
                        color = StatusText,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Demo status only. No real admin review or account approval has occurred.",
            color = StatusSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onOpenAdminDemo,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Outlined.AdminPanelSettings,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Open Admin Demo")
        }
    }
}

@Composable
private fun StatusStep(
    icon: ImageVector,
    title: String,
    subtitle: String,
    completed: Boolean,
    active: Boolean,
    isLast: Boolean = false
) {
    val stepColor = when {
        completed -> Color(0xFF10B981)
        active -> Color(0xFFF59E0B)
        else -> Color(0xFFCBD5E1)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = stepColor.copy(alpha = 0.14f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = stepColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(22.dp)
                )
            }

            if (!isLast) {
                Spacer(
                    Modifier
                        .width(2.dp)
                        .height(35.dp)
                        .background(Color(0xFFCBD5E1))
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = StatusText
            )
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                fontSize = 12.sp,
                color = StatusSecondary
            )
        }
    }
}
