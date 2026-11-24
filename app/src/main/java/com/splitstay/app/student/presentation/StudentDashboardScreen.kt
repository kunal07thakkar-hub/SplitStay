
package com.splitstay.app.student.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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

private val StudentBlue = Color(0xFF2878B8)
private val StudentBackground = Color(0xFFF8FAFC)
private val StudentText = Color(0xFF334155)

@Composable
fun StudentDashboardScreen(
    studentName: String = "Student",
    onSearchClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StudentBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Hello, $studentName 👋",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudentText
                    )
                    Text(
                        "Find a place that feels like home.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDCEBFA),
                    modifier = Modifier.size(46.dp)
                ) {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = StudentBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Where do you want to stay?",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = StudentText
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Search city, area or college") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = "Filters"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearchClick() },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Explore by category",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = StudentText
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    CategoryCard(
                        "Boys PG",
                        Icons.Default.Male,
                        Color(0xFFEAF4FF)
                    ) { onSearchClick() }
                }
                item {
                    CategoryCard(
                        "Girls PG",
                        Icons.Default.Female,
                        Color(0xFFEDE9FE)
                    ) { onSearchClick() }
                }
                item {
                    CategoryCard(
                        "Hostels",
                        Icons.Default.Apartment,
                        Color(0xFFDCFCE7)
                    ) { onSearchClick() }
                }
                item {
                    CategoryCard(
                        "Shared Rooms",
                        Icons.Default.Bed,
                        Color(0xFFFFE4E1)
                    ) { onSearchClick() }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Popular places",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StudentText
                )
                TextButton(onClick = onSearchClick) {
                    Text("See all", color = StudentBlue)
                }
            }

            PropertyPlaceholder(
                "Find your next home",
                "Explore verified PGs and hostels near your college."
            )

            Spacer(modifier = Modifier.height(14.dp))

            PropertyPlaceholder(
                "Comfortable stays, made simple",
                "Compare rent, facilities and available beds."
            )
        }

        NavigationBar(
            containerColor = Color.White
        ) {
            NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                    onSearchClick()
                },
                icon = { Icon(Icons.Default.Search, null) },
                label = { Text("Explore") }
            )
            NavigationBarItem(
                selected = selectedTab == 2,
                onClick = {
                    selectedTab = 2
                    onBookingsClick()
                },
                icon = { Icon(Icons.Default.Bookmark, null) },
                label = { Text("Bookings") }
            )
            NavigationBarItem(
                selected = selectedTab == 3,
                onClick = {
                    selectedTab = 3
                    onProfileClick()
                },
                icon = { Icon(Icons.Default.Person, null) },
                label = { Text("Profile") }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = StudentBlue,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = StudentText
        )
    }
}

@Composable
private fun PropertyPlaceholder(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = StudentText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                description,
                color = Color(0xFF64748B),
                fontSize = 13.sp
            )
        }
    }
}
