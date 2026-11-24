
package com.splitstay.app.student.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccountBlue = Color(0xFF2878B8)
private val AccountBackground = Color(0xFFF8FAFC)
private val AccountText = Color(0xFF334155)
private val AccountGreen = Color(0xFF10B981)
private val AccountOrange = Color(0xFFF59E0B)

// --------------------------------------------------
// 1. MY BOOKINGS
// --------------------------------------------------

@Composable
fun StudentMyBookingsScreen(
    onBookingClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Upcoming") }

    val tabs = listOf(
        "Upcoming", "Pending", "Completed", "Cancelled"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBackground)
            .padding(20.dp)
    ) {
        Text(
            "My Bookings",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AccountText
        )

        Text(
            "Manage your stays and booking requests.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = AccountBackground,
            edgePadding = 0.dp
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = AccountBlue,
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "No $selectedTab bookings yet",
                    fontWeight = FontWeight.SemiBold,
                    color = AccountText
                )
                Text(
                    "Your bookings will appear here.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

// --------------------------------------------------
// 2. BOOKING DETAILS
// --------------------------------------------------

@Composable
fun StudentBookingDetailsScreen(
    bookingId: String,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBackground)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Text(
                "Booking Details",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AccountText
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AccountCard {
            Text(
                "Sunrise Student Residency",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = AccountText
            )
            Text(
                "Booking ID: $bookingId",
                color = AccountBlue,
                modifier = Modifier.padding(top = 8.dp)
            )
            StatusChip("Awaiting confirmation", AccountOrange)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AccountCard {
            Text("Stay Information", fontWeight = FontWeight.Bold)
            AccountInfoRow("Room", "Room A")
            AccountInfoRow("Bed", "A1")
            AccountInfoRow("Check-in", "To be confirmed")
            AccountInfoRow("Check-out", "To be confirmed")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AccountCard {
            Text("Payment Information", fontWeight = FontWeight.Bold)
            AccountInfoRow("Payment status", "Not paid")
            AccountInfoRow("Payment reference", "Not available")
            AccountInfoRow("Total amount", "To be confirmed")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Live booking and payment information will be loaded from your account.",
            color = Color(0xFF64748B),
            fontSize = 12.sp
        )
    }
}

// --------------------------------------------------
// 3. SAVED PROPERTIES
// --------------------------------------------------

@Composable
fun StudentSavedPropertiesScreen(
    onPropertyClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBackground)
            .padding(20.dp)
    ) {
        Text(
            "Saved Properties",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AccountText
        )

        Text(
            "Your favourite stays in one place.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = AccountBlue,
                    modifier = Modifier.size(58.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "No saved properties yet",
                    fontWeight = FontWeight.SemiBold,
                    color = AccountText
                )
                Text(
                    "Tap the heart icon on a property to save it.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

// --------------------------------------------------
// 4. STUDENT PROFILE
// --------------------------------------------------

@Composable
fun StudentProfileScreen(
    studentName: String = "Student",
    email: String = "Not added",
    mobile: String = "Not added",
    onEditProfile: () -> Unit = {},
    onSettings: () -> Unit = {},
    onSupport: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBackground)
            .padding(20.dp)
    ) {
        Text(
            "My Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AccountText
        )

        Spacer(modifier = Modifier.height(24.dp))

        AccountCard {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDCEBFA),
                    modifier = Modifier.size(62.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = AccountBlue,
                        modifier = Modifier.padding(15.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        studentName,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccountText
                    )
                    Text(
                        "Student Account",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AccountCard {
            Text("Personal Information", fontWeight = FontWeight.Bold)
            AccountInfoRow("Email", email)
            AccountInfoRow("Mobile", mobile)
            AccountInfoRow("Email verification", "Pending")
            AccountInfoRow("Mobile verification", "Pending")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AccountMenuItem(
            Icons.Default.Edit,
            "Edit Profile",
            onEditProfile
        )
        AccountMenuItem(
            Icons.Default.Settings,
            "Settings",
            onSettings
        )
        AccountMenuItem(
            Icons.Default.HelpOutline,
            "Help & Support",
            onSupport
        )
    }
}

// --------------------------------------------------
// 5. SUPPORT AND SETTINGS
// --------------------------------------------------

@Composable
fun StudentSupportSettingsScreen(
    onHelpClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBackground)
            .padding(20.dp)
    ) {
        Text(
            "Support & Settings",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = AccountText
        )

        Text(
            "Manage your account and get assistance.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        AccountMenuItem(
            Icons.Default.SupportAgent,
            "Help Centre",
            onHelpClick
        )
        AccountMenuItem(
            Icons.Default.PrivacyTip,
            "Privacy Policy",
            onPrivacyClick
        )
        AccountMenuItem(
            Icons.Default.Description,
            "Terms & Conditions",
            onTermsClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Logout, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = Color(0xFFDC2626))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "SplitStay • A Place to Stay, A Place to Belong.",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp
        )
    }
}

// --------------------------------------------------
// SHARED COMPONENTS
// --------------------------------------------------

@Composable
private fun AccountCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun AccountInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF64748B))
        Text(
            value,
            color = AccountText,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatusChip(
    title: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            title,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 7.dp
            )
        )
    }
}

@Composable
private fun AccountMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = AccountBlue)
            Text(
                title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = AccountText,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = Color(0xFF94A3B8)
            )
        }
    }
}
