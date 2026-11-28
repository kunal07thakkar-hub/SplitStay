
package com.splitstay.app.student.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitstay.app.property.data.PropertyListing
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentExploreScreen(
    properties: List<PropertyListing>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onPropertyClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("Any") }
    var maxRent by remember { mutableFloatStateOf(30000f) }

    val categories = listOf("All", "PG", "Hostel", "Apartment", "Flat", "Co-living")
    val filteredProperties = properties.filter { property ->
        val query = searchQuery.trim().lowercase(Locale.getDefault())

        val matchesSearch = query.isBlank() ||
                property.propertyName.lowercase(Locale.getDefault()).contains(query) ||
                property.city.lowercase(Locale.getDefault()).contains(query) ||
                property.address.lowercase(Locale.getDefault()).contains(query)

        val matchesCategory = selectedCategory == "All" ||
                property.propertyType.equals(selectedCategory, ignoreCase = true)

        val matchesGender = selectedGender == "Any" ||
                property.genderPreference.equals(selectedGender, ignoreCase = true)

        val matchesRent = property.monthlyRent <= maxRent.toDouble()

        matchesSearch && matchesCategory && matchesGender && matchesRent
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore properties") },
                navigationIcon = {
                    OutlinedButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search by property or city") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Property type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.take(3).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.drop(3).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Preferred accommodation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Any", "Boys", "Girls", "Unisex").forEach { gender ->
                    FilterChip(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender },
                        label = { Text(gender) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Maximum monthly rent: ₹${maxRent.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Slider(
                value = maxRent,
                onValueChange = { maxRent = it },
                valueRange = 3000f..30000f
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading && properties.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Loading properties...")
                    }
                }

                errorMessage != null && properties.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onRetry) {
                            Text("Try again")
                        }
                    }
                }

                filteredProperties.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (properties.isEmpty()) {
                                "No properties are listed yet."
                            } else {
                                "No properties match your filters."
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Try changing your search or filters.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (properties.isNotEmpty()) {
                            Text(
                                text = "Clear filters",
                                modifier = Modifier
                                    .clickable {
                                        searchQuery = ""
                                        selectedCategory = "All"
                                        selectedGender = "Any"
                                        maxRent = 30000f
                                    }
                                    .padding(12.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        text = "${filteredProperties.size} properties found",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredProperties,
                            key = { it.id }
                        ) { property ->
                            PropertyResultCard(
                                property = property,
                                onClick = { onPropertyClick(property.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyResultCard(
    property: PropertyListing,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = property.propertyName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${property.propertyType} • ${property.genderPreference}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = listOf(property.address, property.city, property.pincode)
                    .filter { it.isNotBlank() }
                    .joinToString(", "),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            if (property.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = property.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${String.format(Locale("en", "IN"), "%,.0f", property.monthlyRent)} / month",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View details",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}