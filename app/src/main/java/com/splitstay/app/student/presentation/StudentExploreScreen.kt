
package com.splitstay.app.student.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

private val ExploreBlue = Color(0xFF2878B8)
private val ExploreBackground = Color(0xFFF8FAFC)

@Composable
fun StudentExploreScreen(
    onPropertyClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }
    var maxRent by remember { mutableFloatStateOf(15000f) }
    var selectedGender by remember { mutableStateOf("Any") }

    val categories = listOf("All", "Boys PG", "Girls PG", "Hostel")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ExploreBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Explore Stays",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )

        Text(
            "Find a comfortable place to call home.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 18.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city, area or college") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            "Stay category",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedType == category,
                    onClick = { selectedType = category },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            "Maximum monthly rent",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        Text(
            "Up to ₹${maxRent.toInt()} / month",
            color = ExploreBlue,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 5.dp)
        )

        Slider(
            value = maxRent,
            onValueChange = { maxRent = it },
            valueRange = 3000f..30000f,
            steps = 8
        )

        Text(
            "Accommodation preference",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            listOf("Any", "Boys", "Girls").forEach { gender ->
                FilterChip(
                    selected = selectedGender == gender,
                    onClick = { selectedGender = gender },
                    label = { Text(gender) }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Available stays",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF334155)
            )

            Text(
                "Filters",
                color = ExploreBlue,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                EmptyPropertyCard(
                    title = "No listings loaded yet",
                    description = "Verified PGs and hostels will appear here when property owners publish their listings."
                )
            }

            item {
                Text(
                    "Your selected filters will be applied when live property data is connected.",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyPropertyCard(
    title: String,
    description: String
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
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Apartment,
                contentDescription = null,
                tint = ExploreBlue,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                description,
                color = Color(0xFF64748B),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}
