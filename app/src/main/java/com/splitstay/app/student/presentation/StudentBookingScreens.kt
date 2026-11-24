
package com.splitstay.app.student.presentation

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

private val BookingBlue = Color(0xFF2878B8)
private val BookingBackground = Color(0xFFF8FAFC)
private val BookingText = Color(0xFF334155)
private val BookingGreen = Color(0xFF10B981)
private val BookingOrange = Color(0xFFF59E0B)

private enum class BedStatus {
    AVAILABLE, NOTICE, LOCKED, OCCUPIED
}

private data class DemoBed(
    val number: String,
    val status: BedStatus
)

// --------------------------------------------------
// 1. PROPERTY DETAILS SCREEN
// --------------------------------------------------

@Composable
fun StudentPropertyDetailsScreen(
    propertyName: String = "Sunrise Student Residency",
    onSelectBeds: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BookingBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                "Property Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BookingText
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .background(
                            Color(0xFFDCEBFA),
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Apartment,
                            contentDescription = null,
                            tint = BookingBlue,
                            modifier = Modifier.size(55.dp)
                        )
                        Text(
                            "Property photos will appear here",
                            color = BookingBlue,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            item {
                Text(
                    propertyName,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookingText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BookingBlue
                    )
                    Text(
                        "Property location will appear here",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailTag("Verified Property", Color(0xFFDCFCE7))
                    DetailTag("Beds available", Color(0xFFEAF4FF))
                }
            }

            item {
                Text(
                    "About this property",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookingText
                )
                Text(
                    "Property description, house rules and important information will be displayed here once the owner publishes the listing.",
                    color = Color(0xFF64748B),
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Text(
                    "Facilities & Amenities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookingText
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    AmenityRow(Icons.Default.Wifi, "Wi-Fi")
                    AmenityRow(Icons.Default.Restaurant, "Food facilities")
                    AmenityRow(Icons.Default.LocalLaundryService, "Laundry")
                    AmenityRow(Icons.Default.Security, "Security")
                    AmenityRow(Icons.Default.WaterDrop, "Water supply")
                }
            }

            item {
                Text(
                    "Rent & Charges",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookingText
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        PriceRow("Monthly rent", "Shown by owner")
                        PriceRow("Security deposit", "Shown by owner")
                        PriceRow("Additional charges", "As applicable")
                    }
                }
            }
        }

        Button(
            onClick = onSelectBeds,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BookingBlue
            )
        ) {
            Text("View Rooms & Available Beds")
        }
    }
}

// --------------------------------------------------
// 2. ROOM AND BED SELECTION SCREEN
// --------------------------------------------------

@Composable
fun StudentBedSelectionScreen(
    onContinue: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val beds = listOf(
        DemoBed("A1", BedStatus.AVAILABLE),
        DemoBed("A2", BedStatus.OCCUPIED),
        DemoBed("A3", BedStatus.AVAILABLE),
        DemoBed("A4", BedStatus.LOCKED),
        DemoBed("B1", BedStatus.NOTICE),
        DemoBed("B2", BedStatus.AVAILABLE)
    )

    var selectedBed by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BookingBackground)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Choose Your Bed",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BookingText
            )
        }

        Text(
            "Select a room and choose an available bed.",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Room A — Shared Accommodation",
                    fontWeight = FontWeight.SemiBold,
                    color = BookingText
                )
                Text(
                    "Monthly rent: To be confirmed",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    "Select a bed",
                    fontWeight = FontWeight.SemiBold,
                    color = BookingText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    beds.chunked(3).forEach { rowBeds ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowBeds.forEach { bed ->
                                BedCard(
                                    bed = bed,
                                    selected = selectedBed == bed.number,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (
                                            bed.status == BedStatus.AVAILABLE
                                        ) {
                                            selectedBed = bed.number
                                        }
                                    }
                                )
                            }
                            repeat(3 - rowBeds.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Bed status",
            fontWeight = FontWeight.SemiBold,
            color = BookingText
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LegendRow(BookingGreen, "Available")
            LegendRow(BookingOrange, "Notice period")
            LegendRow(Color(0xFFF97316), "Temporarily locked")
            LegendRow(Color(0xFF94A3B8), "Occupied")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onContinue(selectedBed) },
            enabled = selectedBed.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BookingBlue
            )
        ) {
            Text("Continue with Bed $selectedBed")
        }
    }
}

// --------------------------------------------------
// 3. BOOKING SUMMARY SCREEN
// --------------------------------------------------

@Composable
fun StudentBookingSummaryScreen(
    selectedBed: String,
    onConfirmBooking: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var months by remember { mutableIntStateOf(1) }

    // Demonstration values only; actual amounts come from Firestore.
    val monthlyRent = 6000
    val deposit = 6000
    val serviceFee = 200
    val total = monthlyRent * months + deposit + serviceFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BookingBackground)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Booking Summary",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BookingText
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Sunrise Student Residency",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookingText
                )
                Text(
                    "Selected bed: $selectedBed",
                    color = BookingBlue,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider()

                Text(
                    "Booking duration",
                    fontWeight = FontWeight.SemiBold,
                    color = BookingText
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$months month(s)")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (months > 1) months--
                            }
                        ) {
                            Icon(Icons.Default.Remove, null)
                        }
                        Text("$months")
                        IconButton(
                            onClick = {
                                if (months < 12) months++
                            }
                        ) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                }

                HorizontalDivider()

                Text(
                    "Payment breakdown",
                    fontWeight = FontWeight.SemiBold,
                    color = BookingText
                )

                PriceRow("Monthly rent × $months", "₹${monthlyRent * months}")
                PriceRow("Security deposit", "₹$deposit")
                PriceRow("Service fee (demo)", "₹$serviceFee")

                HorizontalDivider()

                PriceRow(
                    "Estimated total",
                    "₹$total",
                    emphasized = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "The final amount, taxes and refundable deposit will be confirmed using the owner's published pricing and booking policy.",
            color = Color(0xFF64748B),
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onConfirmBooking,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BookingBlue
            )
        ) {
            Text("Continue to Booking Confirmation")
        }
    }
}

// --------------------------------------------------
// 4. BOOKING CONFIRMATION SCREEN
// --------------------------------------------------

@Composable
fun StudentBookingConfirmationScreen(
    bookingId: String = "Pending",
    onViewBookings: () -> Unit = {},
    onGoHome: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BookingBackground)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = Color(0xFFDCFCE7),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Booking submitted",
                tint = BookingGreen,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Booking Request Created!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = BookingText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Your booking request has been submitted. You can check its current status in My Bookings.",
            color = Color(0xFF64748B),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    "Booking reference",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
                Text(
                    bookingId,
                    color = BookingBlue,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Status: Awaiting confirmation",
                    color = BookingOrange,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onViewBookings,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BookingBlue
            )
        ) {
            Text("View My Bookings")
        }

        TextButton(onClick = onGoHome) {
            Text("Back to Home", color = BookingBlue)
        }
    }
}

// --------------------------------------------------
// SHARED UI COMPONENTS
// --------------------------------------------------

@Composable
private fun DetailTag(
    title: String,
    backgroundColor: Color
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            title,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            color = BookingText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AmenityRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = BookingBlue)
        Text(
            title,
            modifier = Modifier.padding(start = 12.dp),
            color = BookingText
        )
    }
}

@Composable
private fun PriceRow(
    title: String,
    amount: String,
    emphasized: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            color = if (emphasized) BookingText else Color(0xFF64748B),
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            amount,
            color = BookingText,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun BedCard(
    bed: DemoBed,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bedColor = when (bed.status) {
        BedStatus.AVAILABLE -> BookingGreen
        BedStatus.NOTICE -> BookingOrange
        BedStatus.LOCKED -> Color(0xFFF97316)
        BedStatus.OCCUPIED -> Color(0xFF94A3B8)
    }

    Column(
        modifier = modifier
            .background(
                if (selected) Color(0xFFDCEBFA) else Color.White,
                RoundedCornerShape(14.dp)
            )
            .clickable(
                enabled = bed.status == BedStatus.AVAILABLE,
                onClick = onClick
            )
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Bed,
            contentDescription = null,
            tint = bedColor,
            modifier = Modifier.size(30.dp)
        )
        Text(
            bed.number,
            color = BookingText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            bed.status.name.lowercase().replaceFirstChar { it.uppercase() },
            color = bedColor,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun LegendRow(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Text(
            label,
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
