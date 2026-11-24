
package com.splitstay.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.School
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
fun RoleSelectionScreen(
    onStudentClick: () -> Unit,
    onOwnerClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // SplitStay Branding
            Text(
                text = "SplitStay",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3978B8)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A Place to Stay, A Place to Belong.",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Student Card
            RoleCard(
                title = "I'm a Student",
                description = "Find your perfect PG or hostel.\nExplore rooms and book your bed.",
                icon = {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Student",
                        tint = Color(0xFF3978B8),
                        modifier = Modifier.size(38.dp)
                    )
                },
                cardColor = Color(0xFFEAF4FF),
                borderColor = Color(0xFFBBD7F0),
                onClick = onStudentClick
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Property Owner Card
            RoleCard(
                title = "I'm a Property Owner",
                description = "List your PG or hostel.\nManage beds, bookings and tenants.",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = "Property Owner",
                        tint = Color(0xFF7561B5),
                        modifier = Modifier.size(38.dp)
                    )
                },
                cardColor = Color(0xFFEDE9FE),
                borderColor = Color(0xFFC4B5FD),
                onClick = onOwnerClick
            )
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    cardColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = cardColor,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}
