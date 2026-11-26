
package com.splitstay.app.room.data

enum class BedStatus {
    AVAILABLE,
    LOCKED,
    OCCUPIED,
    NOTICE
}

data class Bed(
    val id: String = "",
    val ownerUid: String = "",
    val bedNumber: String = "",
    val status: String = BedStatus.AVAILABLE.name,
    val monthlyRent: Double = 0.0
)

data class BedDraft(
    val bedNumber: String,
    val monthlyRent: Double
)
