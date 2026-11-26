
package com.splitstay.app.room.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitstay.app.property.data.PropertyListing
import com.splitstay.app.room.data.Bed
import com.splitstay.app.room.data.BedDraft
import com.splitstay.app.room.data.BedStatus
import com.splitstay.app.room.data.Room
import com.splitstay.app.room.data.RoomDraft

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun OwnerRoomBedManagementScreen(
    properties: List<PropertyListing>,
    propertiesLoading: Boolean,
    propertiesError: String?,
    roomBedViewModel: RoomBedViewModel,
    onBack: () -> Unit
) {
    var selectedProperty by remember { mutableStateOf<PropertyListing?>(null) }
    var selectedRoom by remember { mutableStateOf<Room?>(null) }

    val rooms by roomBedViewModel.rooms.collectAsState()
    val beds by roomBedViewModel.beds.collectAsState()
    val isLoading by roomBedViewModel.isLoading.collectAsState()
    val isSaving by roomBedViewModel.isSaving.collectAsState()
    val error by roomBedViewModel.error.collectAsState()
    val message by roomBedViewModel.message.collectAsState()

    var showRoomDialog by remember { mutableStateOf(false) }
    var editingRoom by remember { mutableStateOf<Room?>(null) }
    var showBedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedProperty?.id) {
        selectedProperty?.let {
            selectedRoom = null
            roomBedViewModel.observeRooms(it.id)
        }
    }

    LaunchedEffect(selectedRoom?.id, selectedProperty?.id) {
        val property = selectedProperty
        val room = selectedRoom
        if (property != null && room != null) {
            roomBedViewModel.observeBeds(property.id, room.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            selectedRoom != null -> "Room ${selectedRoom?.roomNumber}"
                            selectedProperty != null -> "Rooms & Beds"
                            else -> "Room & Bed Management"
                        }
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            when {
                                selectedRoom != null -> selectedRoom = null
                                selectedProperty != null -> selectedProperty = null
                                else -> onBack()
                            }
                        }
                    ) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                selectedProperty == null -> {
                    Text(
                        "Choose a property",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Select the property whose rooms and beds you want to manage.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (propertiesLoading) {
                        CircularProgressIndicator()
                    } else if (propertiesError != null) {
                        Text(
                            propertiesError,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else if (properties.isEmpty()) {
                        Text("You haven't added any properties yet.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(properties, key = { it.id }) { property ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedProperty = property
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            property.propertyName,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            property.city,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            property.address,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                selectedRoom == null -> {
                    Text(
                        selectedProperty?.propertyName.orEmpty(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Rooms: ${rooms.size}")

                    Button(
                        onClick = {
                            editingRoom = null
                            showRoomDialog = true
                        },
                        enabled = !isSaving
                    ) {
                        Text("Add room")
                    }

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (rooms.isEmpty()) {
                        Text("No rooms added to this property yet.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(rooms, key = { it.id }) { room ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            "Room ${room.roomNumber}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("${room.roomType} • ${room.floor}")
                                        Text("Capacity: ${room.capacity}")
                                        if (room.description.isNotBlank()) {
                                            Text(room.description)
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { selectedRoom = room }
                                            ) {
                                                Text("Manage beds")
                                            }
                                            OutlinedButton(
                                                onClick = {
                                                    editingRoom = room
                                                    showRoomDialog = true
                                                }
                                            ) {
                                                Text("Edit")
                                            }
                                            TextButton(
                                                onClick = {
                                                    selectedProperty?.let {
                                                        roomBedViewModel.deleteRoom(
                                                            it.id,
                                                            room.id
                                                        )
                                                    }
                                                },
                                                enabled = !isSaving
                                            ) {
                                                Text("Delete")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    val room = selectedRoom!!
                    Text(
                        selectedProperty?.propertyName.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Room ${room.roomNumber}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("${room.roomType} • ${room.floor}")
                    Text("Capacity: ${room.capacity} • Beds added: ${beds.size}")

                    Button(
                        onClick = { showBedDialog = true },
                        enabled = !isSaving
                    ) {
                        Text("Add bed")
                    }

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (beds.isEmpty()) {
                        Text("No beds added to this room yet.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(beds, key = { it.id }) { bed ->
                                BedManagementCard(
                                    bed = bed,
                                    onStatusChange = { status ->
                                        selectedProperty?.let {
                                            roomBedViewModel.updateBedStatus(
                                                it.id,
                                                room.id,
                                                bed.id,
                                                status
                                            )
                                        }
                                    },
                                    onDelete = {
                                        selectedProperty?.let {
                                            roomBedViewModel.deleteBed(
                                                it.id,
                                                room.id,
                                                bed.id
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (error != null) {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (message != null) {
                Text(
                    text = message.orEmpty(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (isSaving) {
                CircularProgressIndicator()
            }
        }
    }

    if (showRoomDialog) {
        RoomEditorDialog(
            room = editingRoom,
            saving = isSaving,
            onDismiss = { showRoomDialog = false },
            onSave = { draft ->
                val propertyId = selectedProperty?.id
                if (propertyId != null) {
                    if (editingRoom == null) {
                        roomBedViewModel.addRoom(propertyId, draft)
                    } else {
                        roomBedViewModel.updateRoom(
                            propertyId,
                            editingRoom!!,
                            draft
                        )
                    }
                }
                showRoomDialog = false
            }
        )
    }

    if (showBedDialog && selectedProperty != null && selectedRoom != null) {
        BedEditorDialog(
            saving = isSaving,
            onDismiss = { showBedDialog = false },
            onSave = { draft ->
                roomBedViewModel.addBed(
                    selectedProperty!!.id,
                    selectedRoom!!.id,
                    draft
                )
                showBedDialog = false
            }
        )
    }
}

@Composable
private fun RoomEditorDialog(
    room: Room?,
    saving: Boolean,
    onDismiss: () -> Unit,
    onSave: (RoomDraft) -> Unit
) {
    var roomNumber by remember(room?.id) {
        mutableStateOf(room?.roomNumber.orEmpty())
    }
    var floor by remember(room?.id) {
        mutableStateOf(room?.floor.orEmpty())
    }
    var roomType by remember(room?.id) {
        mutableStateOf(room?.roomType ?: "Shared")
    }
    var capacity by remember(room?.id) {
        mutableStateOf(room?.capacity?.toString() ?: "1")
    }
    var description by remember(room?.id) {
        mutableStateOf(room?.description.orEmpty())
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (room == null) "Add room" else "Edit room") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    label = { Text("Room number") },
                    enabled = room == null,
                    singleLine = true
                )
                OutlinedTextField(
                    value = floor,
                    onValueChange = { floor = it },
                    label = { Text("Floor") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = roomType,
                    onValueChange = { roomType = it },
                    label = { Text("Room type (e.g. Single, Shared)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it.filter(Char::isDigit) },
                    label = { Text("Room capacity") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                if (validationError != null) {
                    Text(
                        validationError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !saving,
                onClick = {
                    val parsedCapacity = capacity.toIntOrNull()
                    if (roomNumber.isBlank() || floor.isBlank() ||
                        roomType.isBlank() || parsedCapacity == null ||
                        parsedCapacity !in 1..30
                    ) {
                        validationError =
                            "Enter a room number, floor, room type and capacity from 1 to 30."
                    } else {
                        onSave(
                            RoomDraft(
                                roomNumber = roomNumber.trim(),
                                floor = floor.trim(),
                                roomType = roomType.trim(),
                                capacity = parsedCapacity,
                                description = description.trim()
                            )
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun BedEditorDialog(
    saving: Boolean,
    onDismiss: () -> Unit,
    onSave: (BedDraft) -> Unit
) {
    var bedNumber by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add bed") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = bedNumber,
                    onValueChange = { bedNumber = it },
                    label = { Text("Bed number") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = rent,
                    onValueChange = { rent = it },
                    label = { Text("Monthly rent (₹)") },
                    singleLine = true
                )
                if (validationError != null) {
                    Text(
                        validationError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !saving,
                onClick = {
                    val amount = rent.toDoubleOrNull()
                    if (bedNumber.isBlank() || amount == null || amount < 0) {
                        validationError =
                            "Enter a bed number and a valid non-negative rent."
                    } else {
                        onSave(
                            BedDraft(
                                bedNumber = bedNumber.trim(),
                                monthlyRent = amount
                            )
                        )
                    }
                }
            ) {
                Text("Save bed")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun BedManagementCard(
    bed: Bed,
    onStatusChange: (BedStatus) -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val status = BedStatus.entries.firstOrNull {
        it.name == bed.status
    } ?: BedStatus.AVAILABLE

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Bed ${bed.bedNumber}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Monthly rent: ₹${bed.monthlyRent.toInt()}")
            Text("Status: ${status.name.replace('_', ' ')}")

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { menuExpanded = true }) {
                    Text("Change status")
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    BedStatus.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.name.replace('_', ' '))
                            },
                            onClick = {
                                menuExpanded = false
                                onStatusChange(option)
                            }
                        )
                    }
                }

                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}
