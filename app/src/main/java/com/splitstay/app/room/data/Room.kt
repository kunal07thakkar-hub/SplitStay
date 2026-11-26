
package com.splitstay.app.room.data

data class Room(
    val id: String = "",
    val ownerUid: String = "",
    val roomNumber: String = "",
    val floor: String = "",
    val roomType: String = "",
    val capacity: Int = 1,
    val description: String = ""
)

data class RoomDraft(
    val roomNumber: String,
    val floor: String,
    val roomType: String,
    val capacity: Int,
    val description: String
)
