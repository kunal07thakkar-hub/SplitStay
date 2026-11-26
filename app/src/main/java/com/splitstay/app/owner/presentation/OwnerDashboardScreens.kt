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
import com.splitstay.app.property.data.PropertyListing
import com.splitstay.app.property.data.PropertyDraft

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
    propertyCount: Int = 0,
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
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Unable to load owner information",
                                fontWeight = FontWeight.SemiBold,
                                color = OwnerText
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                errorMessage,
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )

                            TextButton(
                                onClick = onRetry
                            ) {
                                Text(
                                    "Try again",
                                    color = OwnerBlue
                                )
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

                    IconButton(
                        onClick = onProfileClick
                    ) {
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
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OwnerStatCard(
                            "Properties",
                            propertyCount.toString(),
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
        }
    }
}

// --------------------------------------------------
// 2. MY PROPERTIES
// --------------------------------------------------

@Composable
fun OwnerMyPropertiesScreen(
    properties: List<PropertyListing>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onAddProperty: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "My Properties",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )

                Text(
                    "Manage your PGs and hostels",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }

            FilledTonalButton(
                onClick = onAddProperty,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFDCEBFA),
                    contentColor = OwnerBlue
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text("Add")
            }
        }

        when {
            isLoading && properties.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = OwnerBlue
                    )
                }
            }

            errorMessage != null && properties.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = OwnerOrange,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        "Unable to load properties",
                        fontWeight = FontWeight.SemiBold,
                        color = OwnerText
                    )

                    Text(
                        errorMessage,
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OwnerBlue
                        )
                    ) {
                        Text("Try again")
                    }
                }
            }

            properties.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Apartment,
                        contentDescription = null,
                        tint = OwnerBlue,
                        modifier = Modifier.size(58.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        "No properties yet",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = OwnerText
                    )

                    Text(
                        "Add your first PG, hostel or apartment to get started.",
                        color = Color(0xFF64748B),
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Button(
                        onClick = onAddProperty,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OwnerBlue
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text("Add Property")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 4.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = properties,
                        key = { it.id }
                    ) { property ->
                        PropertyListingCard(
                            property = property
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyListingCard(
    property: PropertyListing
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        property.propertyName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OwnerText
                    )

                    Text(
                        "${property.propertyType} • ${property.genderPreference}",
                        fontSize = 12.sp,
                        color = OwnerBlue
                    )
                }

                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        "Listed",
                        color = Color(0xFF15803D),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(17.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    "${property.address}, ${property.city} - ${property.pincode}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            if (property.description.isNotBlank()) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    property.description,
                    fontSize = 13.sp,
                    color = OwnerText
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFE2E8F0)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Monthly rent",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )

                Text(
                    "₹${property.monthlyRent.toInt()} / month",
                    color = OwnerBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// --------------------------------------------------
// 3. ADD PROPERTY
// --------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerAddPropertyScreen(
    isSaving: Boolean,
    errorMessage: String?,
    onSaveProperty: (PropertyDraft) -> Unit,
    onBack: () -> Unit
) {
    var propertyName by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("PG") }
    var genderPreference by remember { mutableStateOf("Boys") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var expandedType by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val propertyTypes = listOf(
        "PG",
        "Hostel",
        "Apartment",
        "Flat",
        "Co-living"
    )

    val genderOptions = listOf(
        "Boys",
        "Girls",
        "Unisex"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                enabled = !isSaving
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = OwnerText
                )
            }

            Column {
                Text(
                    "Add Property",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )

                Text(
                    "Enter your property details",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFE2E8F0)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "Basic information",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField(
                    value = propertyName,
                    onValueChange = {
                        propertyName = it
                        validationError = null
                    },
                    label = "Property name",
                    placeholder = "e.g. Sunrise Boys PG"
                )
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = expandedType,
                    onExpandedChange = {
                        expandedType = !expandedType
                    }
                ) {
                    OutlinedTextField(
                        value = propertyType,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Property type")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedType
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OwnerBlue
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = {
                            expandedType = false
                        }
                    ) {
                        propertyTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(type)
                                },
                                onClick = {
                                    propertyType = type
                                    expandedType = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = {
                        expandedGender = !expandedGender
                    }
                ) {
                    OutlinedTextField(
                        value = genderPreference,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Suitable for")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedGender
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OwnerBlue
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = {
                            expandedGender = false
                        }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(option)
                                },
                                onClick = {
                                    genderPreference = option
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Location details",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField(
                    value = address,
                    onValueChange = {
                        address = it
                        validationError = null
                    },
                    label = "Full address",
                    placeholder = "House number, street, area"
                )
            }

            item {
                OwnerInputField(
                    value = city,
                    onValueChange = {
                        city = it
                        validationError = null
                    },
                    label = "City",
                    placeholder = "e.g. Ahmedabad"
                )
            }

            item {
                OwnerInputField(
                    value = pincode,
                    onValueChange = {
                        if (it.length <= 6 && it.all(Char::isDigit)) {
                            pincode = it
                            validationError = null
                        }
                    },
                    label = "PIN code",
                    placeholder = "6-digit PIN code",
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                Text(
                    "Rent and description",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OwnerText
                )
            }

            item {
                OwnerInputField(
                    value = monthlyRent,
                    onValueChange = {
                        monthlyRent = it
                        validationError = null
                    },
                    label = "Monthly rent (₹)",
                    placeholder = "e.g. 6500",
                    keyboardType = KeyboardType.Decimal
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Description (optional)")
                    },
                    placeholder = {
                        Text("Tell students about your property")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OwnerBlue
                    )
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF4FF)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = OwnerBlue
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text(
                            "Property photos will be available in a later update. This version saves property details only.",
                            color = OwnerText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (validationError != null) {
                item {
                    Text(
                        validationError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }

            if (errorMessage != null) {
                item {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        val rentValue = monthlyRent.toDoubleOrNull()

                        when {
                            propertyName.isBlank() -> {
                                validationError =
                                    "Please enter the property name."
                            }

                            address.isBlank() -> {
                                validationError =
                                    "Please enter the full address."
                            }

                            city.isBlank() -> {
                                validationError =
                                    "Please enter the city."
                            }

                            pincode.length != 6 -> {
                                validationError =
                                    "Please enter a valid 6-digit PIN code."
                            }

                            rentValue == null || rentValue <= 0.0 -> {
                                validationError =
                                    "Please enter a valid monthly rent."
                            }

                            else -> {
                                validationError = null

                                onSaveProperty(
                                    PropertyDraft(
                                        propertyName = propertyName.trim(),
                                        propertyType = propertyType,
                                        genderPreference = genderPreference,
                                        address = address.trim(),
                                        city = city.trim(),
                                        pincode = pincode,
                                        description = description.trim(),
                                        monthlyRent = rentValue
                                    )
                                )
                            }
                        }
                    },
                    enabled = !isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OwnerBlue
                    )
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text("Saving...")
                    } else {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Save Property")
                    }
                }
            }
        }
    }
}

// --------------------------------------------------
// 4. ROOM & BED MANAGEMENT
// --------------------------------------------------

@Composable
fun OwnerRoomBedManagementScreen(
    onBack: () -> Unit = {}
) {
    var selectedTab by remember {
        mutableStateOf("Rooms")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OwnerBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                "Room & Bed Management",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OwnerText
            )
        }

        TabRow(
            selectedTabIndex = if (selectedTab == "Rooms") 0 else 1,
            containerColor = Color.White,
            contentColor = OwnerBlue
        ) {
            listOf("Rooms", "Beds").forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = {
                        selectedTab = tab
                    },
                    text = {
                        Text(tab)
                    }
                )
            }
        }

        if (selectedTab == "Rooms") {
            EmptyManagementState(
                title = "Room management",
                message = "Room creation and editing will be connected to your property in a later milestone.",
                icon = Icons.Default.MeetingRoom
            )
        } else {
            EmptyManagementState(
                title = "Bed management",
                message = "Bed availability and occupancy management will be connected in a later milestone.",
                icon = Icons.Default.Bed
            )
        }
    }
}

// --------------------------------------------------
// 5. EMPTY MANAGEMENT STATE
// --------------------------------------------------

@Composable
private fun EmptyManagementState(
    title: String,
    message: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = OwnerBlue,
            modifier = Modifier.size(54.dp)
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            title,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = OwnerText
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            message,
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )
    }
}

// --------------------------------------------------
// 6. OWNER STAT CARD
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = OwnerBlue,
                modifier = Modifier.size(24.dp)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OwnerText
            )

            Text(
                title,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

// --------------------------------------------------
// 7. OWNER ACTION ITEM
// --------------------------------------------------

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
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEAF4FF)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OwnerBlue,
                    modifier = Modifier
                        .padding(11.dp)
                        .size(23.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    color = OwnerText,
                    fontSize = 14.sp
                )

                Text(
                    subtitle,
                    color = Color(0xFF64748B),
                    fontSize = 11.sp
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8)
            )
        }
    }
}

// --------------------------------------------------
// 8. OWNER INPUT FIELD
// --------------------------------------------------

@Composable
private fun OwnerInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OwnerBlue
        )
    )
}

// --------------------------------------------------
// 9. STATUS CHIP
// --------------------------------------------------

@Composable
private fun StatusChip(
    status: String
) {
    val chipColor = when (status.uppercase()) {
        "AVAILABLE" -> OwnerGreen
        "NOTICE" -> OwnerOrange
        "LOCKED" -> Color(0xFFF97316)
        "OCCUPIED" -> Color(0xFF94A3B8)
        else -> OwnerBlue
    }

    Surface(
        color = chipColor.copy(alpha = 0.12f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            status,
            color = chipColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
        )
    }
}