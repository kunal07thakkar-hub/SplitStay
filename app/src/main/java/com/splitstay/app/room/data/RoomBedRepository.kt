
package com.splitstay.app.room.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RoomBedRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun currentUid(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("Please sign in again.")
    }

    private suspend fun verifyOwner(propertyId: String): String {
        val user = auth.currentUser
            ?: throw IllegalStateException("Please sign in again.")

        user.reload().await()

        if (user.isEmailVerified.not()) {
            throw IllegalStateException("Please verify your email.")
        }

        val uid = user.uid
        val profile = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        if (profile.getString("role") != "OWNER" ||
            profile.getString("accountStatus") != "APPROVED"
        ) {
            throw IllegalStateException("Only approved owners can manage rooms.")
        }

        val property = firestore.collection("properties")
            .document(propertyId)
            .get()
            .await()

        if (property.getString("ownerUid") != uid) {
            throw IllegalStateException("You do not own this property.")
        }

        return uid
    }

    private fun safeDocumentId(number: String): String {
        return number.trim()
            .uppercase()
            .replace(Regex("[^A-Z0-9_-]"), "_")
    }

    private fun roomsCollection(propertyId: String) =
        firestore.collection("properties")
            .document(propertyId)
            .collection("rooms")

    private fun bedsCollection(propertyId: String, roomId: String) =
        roomsCollection(propertyId)
            .document(roomId)
            .collection("beds")

    fun observeRooms(propertyId: String): Flow<List<Room>> = callbackFlow {
        var registration: ListenerRegistration? = null

        try {
            verifyOwner(propertyId)

            registration = roomsCollection(propertyId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val rooms = snapshot?.documents.orEmpty().map { doc ->
                        Room(
                            id = doc.id,
                            ownerUid = doc.getString("ownerUid").orEmpty(),
                            roomNumber = doc.getString("roomNumber").orEmpty(),
                            floor = doc.getString("floor").orEmpty(),
                            roomType = doc.getString("roomType").orEmpty(),
                            capacity = (doc.getLong("capacity") ?: 1L).toInt(),
                            description = doc.getString("description").orEmpty()
                        )
                    }.sortedBy { it.roomNumber }

                    trySend(rooms)
                }
        } catch (e: Exception) {
            close(e)
        }

        awaitClose { registration?.remove() }
    }

    fun observeBeds(
        propertyId: String,
        roomId: String
    ): Flow<List<Bed>> = callbackFlow {
        var registration: ListenerRegistration? = null

        try {
            verifyOwner(propertyId)

            registration = bedsCollection(propertyId, roomId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val beds = snapshot?.documents.orEmpty().map { doc ->
                        Bed(
                            id = doc.id,
                            ownerUid = doc.getString("ownerUid").orEmpty(),
                            bedNumber = doc.getString("bedNumber").orEmpty(),
                            status = doc.getString("status")
                                ?: BedStatus.AVAILABLE.name,
                            monthlyRent = doc.getDouble("monthlyRent") ?: 0.0
                        )
                    }.sortedBy { it.bedNumber }

                    trySend(beds)
                }
        } catch (e: Exception) {
            close(e)
        }

        awaitClose { registration?.remove() }
    }

    suspend fun addRoom(
        propertyId: String,
        draft: RoomDraft
    ) {
        val uid = verifyOwner(propertyId)
        val roomId = safeDocumentId(draft.roomNumber)

        require(roomId.isNotBlank()) { "Enter a valid room number." }

        val roomRef = roomsCollection(propertyId).document(roomId)

        firestore.runTransaction { transaction ->
            val existing = transaction.get(roomRef)

            if (existing.exists()) {
                throw IllegalStateException("This room number already exists.")
            }

            val now = Timestamp.now()

            transaction.set(
                roomRef,
                mapOf(
                    "ownerUid" to uid,
                    "roomNumber" to draft.roomNumber.trim(),
                    "floor" to draft.floor.trim(),
                    "roomType" to draft.roomType.trim(),
                    "capacity" to draft.capacity,
                    "description" to draft.description.trim(),
                    "createdAt" to now,
                    "updatedAt" to now
                )
            )
        }.await()
    }

    suspend fun updateRoom(
        propertyId: String,
        room: Room,
        draft: RoomDraft
    ) {
        verifyOwner(propertyId)

        require(room.id.isNotBlank()) { "Invalid room." }

        roomsCollection(propertyId)
            .document(room.id)
            .update(
                mapOf(
                    "floor" to draft.floor.trim(),
                    "roomType" to draft.roomType.trim(),
                    "capacity" to draft.capacity,
                    "description" to draft.description.trim(),
                    "updatedAt" to Timestamp.now()
                )
            ).await()
    }

    suspend fun deleteRoom(
        propertyId: String,
        roomId: String
    ) {
        verifyOwner(propertyId)

        val roomRef = roomsCollection(propertyId).document(roomId)
        val beds = roomRef.collection("beds").get().await()

        if (!beds.isEmpty) {
            throw IllegalStateException(
                "Remove all beds from this room before deleting it."
            )
        }

        roomRef.delete().await()
    }

    suspend fun addBed(
        propertyId: String,
        roomId: String,
        draft: BedDraft
    ) {
        val uid = verifyOwner(propertyId)
        val bedId = safeDocumentId(draft.bedNumber)

        require(bedId.isNotBlank()) { "Enter a valid bed number." }
        require(draft.monthlyRent >= 0) { "Rent cannot be negative." }

        val roomRef = roomsCollection(propertyId).document(roomId)
        val bedRef = bedsCollection(propertyId, roomId).document(bedId)

        firestore.runTransaction { transaction ->
            val room = transaction.get(roomRef)
            if (!room.exists()) {
                throw IllegalStateException("Room not found.")
            }

            val existing = transaction.get(bedRef)
            if (existing.exists()) {
                throw IllegalStateException("This bed number already exists.")
            }

            val now = Timestamp.now()

            transaction.set(
                bedRef,
                mapOf(
                    "ownerUid" to uid,
                    "bedNumber" to draft.bedNumber.trim(),
                    "status" to BedStatus.AVAILABLE.name,
                    "monthlyRent" to draft.monthlyRent,
                    "createdAt" to now,
                    "updatedAt" to now
                )
            )
        }.await()
    }

    suspend fun updateBedStatus(
        propertyId: String,
        roomId: String,
        bedId: String,
        status: BedStatus
    ) {
        verifyOwner(propertyId)

        bedsCollection(propertyId, roomId)
            .document(bedId)
            .update(
                mapOf(
                    "status" to status.name,
                    "updatedAt" to Timestamp.now()
                )
            ).await()
    }

    suspend fun deleteBed(
        propertyId: String,
        roomId: String,
        bedId: String
    ) {
        verifyOwner(propertyId)

        bedsCollection(propertyId, roomId)
            .document(bedId)
            .delete()
            .await()
    }
}
