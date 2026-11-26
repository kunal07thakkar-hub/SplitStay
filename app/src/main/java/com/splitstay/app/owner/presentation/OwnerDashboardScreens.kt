
package com.splitstay.app.owner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OwnerBlue = Color(0xFF2878B8)
private val OwnerBackground = Color(0xFFF8FAFC)
private val OwnerText = Color(0xFF334155)
private val OwnerGreen = Color(0xFF10B981)
private val OwnerOrange = Color(0xFFF59E0B)

// --------------------------------------------------
// 1. OWNER DASHBOARD
// --------------------------------------------------

@Composable
fun OwnerDashboardScreen(
    ownerName: String = "Property Owner",
    propertyName: String = "",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {},
    onPropertiesClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    onBedsClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = OwnerBlue
                    )
                }
            }

            if (errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFE4E1)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Unable to load owner profile",
                                fontWeight = FontWeight.SemiBold,
                                color = OwnerText
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                errorMessage,
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                            TextButton(onClick = onRetry) {
                                Text("Try again", color = OwnerBlue)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Hello, $ownerName 👋",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = OwnerText
                        )
                        Text(
                            "Here's your property overview.",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }

                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = OwnerBlue,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF4FF)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            "Your Property Business",
                            color = OwnerText,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (propertyName.isNotBlank()) {
                            Text(
                                propertyName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = OwnerBlue,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Text(
                            "Manage your PGs, hostels and bookings from one place.",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    "Overview",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OwnerStatCard(
                            "Properties",
                            "0",
                            Icons.Default.Apartment,
                            Modifier.weight(1f)
                        )
                        OwnerStatCard(
                            "Total Beds",
                            "0",
                            Icons.Default.Bed,
                            Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OwnerStatCard(
                            "Occupied",
                            "0",
                            Icons.Default.People,
                            Modifier.weight(1f)
                        )
                        OwnerStatCard(
                            "Available",
                            "0",
                            Icons.Default.CheckCircle,
                            Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Text(
                    "Quick Management",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OwnerActionItem(
                        Icons.Default.Apartment,
                        "My Properties",
                        "Manage your listed properties",
                        onPropertiesClick
                    )
                    OwnerActionItem(
                        Icons.Default.Bookmark,
                        "Booking Requests",
                        "Review student booking requests",
                        onBookingsClick
                    )
                    OwnerActionItem(
                        Icons.Default.Bed,
                        "Room & Bed Management",
                        "Manage rooms and availability",
                        onBedsClick
                    )
                    OwnerActionItem(
                        Icons.Default.AccountBalanceWallet,
                        "Earnings",
                        "View collections and payments",
                        onEarningsClick
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Your account approval status",
                        fontWeight = FontWeight.SemiBold,
                        color = OwnerText
                    )
                    StatusChip("Account approved", OwnerGreen)
                    Text(
                        "Your account has been approved. You can proceed with property management.",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                }
            }
        }

        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = false,
                onClick = onPropertiesClick,
                icon = { Icon(Icons.Default.Apartment, null) },
                label = { Text("Properties") }
            )
            NavigationBarItem(
                selected = false,
                onClick = onBookingsClick,
                icon = { Icon(Icons.Default.Bookmark, null) },
                label = { Text("Bookings") }
            )
            NavigationBarItem(
                selected = false,
                onClick = onProfileClick,
                icon = { Icon(Icons.Default.Person, null) },
                label = { Text("Profile") }
            )
        }
    }
}

// --------------------------------------------------
// 2. MY PROPERTIES
// --------------------------------------------------

@Composable
fun OwnerMyPropertiesScreen(
    onAddProperty: () -> Unit = {},
    onPropertyClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "My Properties",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = OwnerText
            )

            IconButton(onClick = onAddProperty) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Add property",
                    tint = OwnerBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            "Manage all your PGs and hostels.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 22.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Apartment,
                    contentDescription = null,
                    tint = OwnerBlue,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "No properties added yet",
                    fontWeight = FontWeight.SemiBold,
                    color = OwnerText
                )

                Text(
                    "Add your first PG or hostel to get started.",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onAddProperty,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OwnerBlue
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Property")
                }
            }
        }
    }
}

// --------------------------------------------------
// 3. ADD / EDIT PROPERTY
// --------------------------------------------------

@Composable
fun OwnerAddPropertyScreen(
    onSaveProperty: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var propertyName by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("PG") }
    var genderPreference by remember { mutableStateOf("Boys") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }

    val propertyTypes = listOf("PG", "Hostel", "Apartment")
    val genderTypes = listOf("Boys", "Girls", "Co-living")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Text(
                "Add Property",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OwnerText
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "Property Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField("Property / PG Name", propertyName) {
                    propertyName = it
                }
            }

            item {
                Text("Property Type", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    propertyTypes.forEach { type ->
                        FilterChip(
                            selected = propertyType == type,
                            onClick = { propertyType = type },
                            label = { Text(type) }
                        )
                    }
                }
            }

            item {
                Text("Accommodation Type", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    genderTypes.forEach { type ->
                        FilterChip(
                            selected = genderPreference == type,
                            onClick = { genderPreference = type },
                            label = { Text(type) }
                        )
                    }
                }
            }

            item {
                Text(
                    "Property Location",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField("Complete Address", address) {
                    address = it
                }
            }

            item {
                OwnerInputField("City", city) {
                    city = it
                }
            }

            item {
                OwnerInputField(
                    "PIN Code",
                    pincode,
                    KeyboardType.Number
                ) {
                    pincode = it
                }
            }

            item {
                Text(
                    "Rent & Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField(
                    "Starting Monthly Rent (₹)",
                    monthlyRent,
                    KeyboardType.Number
                ) {
                    monthlyRent = it
                }
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Property Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Text(
                    "Property Photos",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedButton(
                    onClick = {
                        // Photo picker will be connected later.
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Property Photos")
                }
            }
        }

        Button(
            onClick = onSaveProperty,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OwnerBlue
            )
        ) {
            Text("Save Property Details")
        }
    }
}

// --------------------------------------------------
// 4. ROOM & BED MANAGEMENT
// --------------------------------------------------

@Composable
fun OwnerRoomBedManagementScreen(
    onAddRoom: () -> Unit = {},
    onSaveChanges: () -> Unit = {}
) {
    var roomName by remember { mutableStateOf("Room A") }
    var bedCount by remember { mutableIntStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
            .padding(20.dp)
    ) {
        Text(
            "Room & Bed Management",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = OwnerText
        )

        Text(
            "Manage room layouts and bed availability.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    "Room Configuration",
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    label = { Text("Room Name / Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Number of Beds", fontWeight = FontWeight.SemiBold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (bedCount > 1) bedCount--
                        }
                    ) {
                        Icon(Icons.Default.RemoveCircleOutline, null)
                    }

                    Text(
                        "$bedCount Beds",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OwnerBlue
                    )

                    IconButton(
                        onClick = {
                            if (bedCount < 20) bedCount++
                        }
                    ) {
                        Icon(Icons.Default.AddCircleOutline, null)
                    }
                }

                Button(
                    onClick = onAddRoom,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OwnerBlue
                    )
                ) {
                    Text("Add Room")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Bed Availability Preview",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = OwnerText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(bedCount) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Bed,
                        null,
                        tint = OwnerGreen
                    )

                    Text(
                        "Bed ${index + 1}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        color = OwnerText,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        "Available",
                        color = OwnerGreen,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSaveChanges,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OwnerBlue
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Save Room & Bed Configuration")
        }
    }
}

// --------------------------------------------------
// SHARED COMPONENTS
// --------------------------------------------------

@Composable
private fun OwnerStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = OwnerBlue)
            Text(
                value,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = OwnerText,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                title,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun OwnerActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = OwnerBlue)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(title, fontWeight = FontWeight.SemiBold, color = OwnerText)
                Text(
                    subtitle,
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

@Composable
private fun OwnerInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
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
