
package com.splitstay.app.owner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

private val ManagementBlue = Color(0xFF2878B8)
private val ManagementBackground = Color(0xFFF8FAFC)
private val ManagementText = Color(0xFF334155)
private val ManagementGreen = Color(0xFF10B981)
private val ManagementOrange = Color(0xFFF59E0B)

// --------------------------------------------------
// 1. BOOKING MANAGEMENT
// --------------------------------------------------

@Composable
fun OwnerBookingManagementScreen(
    onBookingClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Pending") }
    val tabs = listOf("Pending", "Confirmed", "Completed", "Cancelled")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Booking Management",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "Manage student booking requests and stays.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = ManagementBackground,
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

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Bookmarks,
                    null,
                    tint = ManagementBlue,
                    modifier = Modifier.size(58.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "No $selectedTab bookings",
                    fontWeight = FontWeight.SemiBold,
                    color = ManagementText
                )
                Text(
                    "Student booking requests will appear here.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

// --------------------------------------------------
// 2. TENANT MANAGEMENT
// --------------------------------------------------

@Composable
fun OwnerTenantManagementScreen(
    onTenantClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Tenant Management",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "Manage your residents and their stay details.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search tenant by name") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ManagementStat(
                "Active Tenants",
                "0",
                Modifier.weight(1f)
            )
            ManagementStat(
                "Vacant Beds",
                "0",
                Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Residents",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Tenant records will appear here after confirmed bookings.",
                color = Color(0xFF64748B),
                fontSize = 13.sp
            )
        }
    }
}

// --------------------------------------------------
// 3. EARNINGS DASHBOARD
// --------------------------------------------------

@Composable
fun OwnerEarningsScreen(
    onTransactionsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Earnings",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "Track your property income and collections.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEAF4FF)
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    "Total Earnings",
                    color = Color(0xFF64748B)
                )
                Text(
                    "₹0",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = ManagementBlue,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    "No payment records available yet",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ManagementStat(
                "This Month",
                "₹0",
                Modifier.weight(1f)
            )
            ManagementStat(
                "Pending",
                "₹0",
                Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ManagementStat(
                "Collected",
                "₹0",
                Modifier.weight(1f)
            )
            ManagementStat(
                "Refunds",
                "₹0",
                Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onTransactionsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManagementBlue
            )
        ) {
            Icon(Icons.Default.ReceiptLong, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Transaction History")
        }
    }
}

// --------------------------------------------------
// 4. TRANSACTION HISTORY
// --------------------------------------------------

@Composable
fun OwnerTransactionHistoryScreen(
    onTransactionClick: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Transactions",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "View your payment and refund records.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Received", "Refunded").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.ReceiptLong,
                    null,
                    tint = ManagementBlue,
                    modifier = Modifier.size(58.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "No transactions yet",
                    fontWeight = FontWeight.SemiBold,
                    color = ManagementText
                )
                Text(
                    "Payment records will appear here.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

// --------------------------------------------------
// 5. OWNER PROFILE
// --------------------------------------------------

@Composable
fun OwnerProfileScreen(
    ownerName: String = "Property Owner",
    email: String = "Not added",
    mobile: String = "Not added",
    onEditProfile: () -> Unit = {},
    onKycClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Owner Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Spacer(modifier = Modifier.height(22.dp))

        ManagementCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDCEBFA),
                    modifier = Modifier.size(65.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = ManagementBlue,
                        modifier = Modifier.padding(15.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        ownerName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ManagementText
                    )
                    Text(
                        "Property Owner",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ManagementCard {
            Text(
                "Contact Information",
                fontWeight = FontWeight.Bold
            )
            ManagementInfo("Email", email)
            ManagementInfo("Mobile", mobile)
            ManagementInfo("Email verification", "Pending")
            ManagementInfo("Mobile verification", "Pending")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ManagementMenu(
            Icons.Default.Edit,
            "Edit Profile",
            onEditProfile
        )
        ManagementMenu(
            Icons.Default.VerifiedUser,
            "KYC & Verification",
            onKycClick
        )
        ManagementMenu(
            Icons.Default.Settings,
            "Account Settings",
            onSettingsClick
        )
    }
}

// --------------------------------------------------
// 6. OWNER NOTIFICATIONS
// --------------------------------------------------

@Composable
fun OwnerNotificationsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Notifications",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "Stay updated about your properties and bookings.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.NotificationsNone,
                    null,
                    tint = ManagementBlue,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "You're all caught up!",
                    fontWeight = FontWeight.SemiBold,
                    color = ManagementText
                )
                Text(
                    "New booking and account updates will appear here.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

// --------------------------------------------------
// 7. OWNER SUPPORT & SETTINGS
// --------------------------------------------------

@Composable
fun OwnerSupportSettingsScreen(
    onHelpClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ManagementBackground)
            .padding(20.dp)
    ) {
        Text(
            "Support & Settings",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = ManagementText
        )

        Text(
            "Manage your account and get assistance.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        ManagementMenu(
            Icons.Default.SupportAgent,
            "Help Centre",
            onHelpClick
        )
        ManagementMenu(
            Icons.Default.PrivacyTip,
            "Privacy Policy",
            onPrivacyClick
        )
        ManagementMenu(
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
            Text(
                "Logout",
                color = Color(0xFFDC2626)
            )
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
private fun ManagementStat(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ManagementBlue
            )
            Text(
                title,
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ManagementCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun ManagementInfo(
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
            color = ManagementText,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ManagementMenu(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
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
            Icon(icon, null, tint = ManagementBlue)
            Text(
                title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = ManagementText,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = Color.Gray
            )
        }
    }
}
