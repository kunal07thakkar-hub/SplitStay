package com.splitstay.app.property.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PropertyRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    private val propertiesCollection
        get() = firestore.collection("properties")

    suspend fun addProperty(draft: PropertyDraft) {
        val user = firebaseAuth.currentUser
            ?: throw IllegalStateException("Please sign in again.")

        user.reload().await()

        if (!user.isEmailVerified) {
            throw IllegalStateException(
                "Please verify your email before adding a property."
            )
        }

        val profileSnapshot = firestore.collection("users")
            .document(user.uid)
            .get()
            .await()

        val role = profileSnapshot.getString("role")
        val accountStatus = profileSnapshot.getString("accountStatus")

        if (role != "OWNER" || accountStatus != "APPROVED") {
            throw IllegalStateException(
                "Only approved property-owner accounts can add properties."
            )
        }

        val propertyDocument = propertiesCollection.document()

        val propertyData = hashMapOf(
            "ownerUid" to user.uid,
            "propertyName" to draft.propertyName,
            "propertyType" to draft.propertyType,
            "genderPreference" to draft.genderPreference,
            "address" to draft.address,
            "city" to draft.city,
            "pincode" to draft.pincode,
            "description" to draft.description,
            "monthlyRent" to draft.monthlyRent,
            "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

        propertyDocument.set(propertyData).await()
    }

    fun observeOwnerProperties(): Flow<List<PropertyListing>> = callbackFlow {
        val user = firebaseAuth.currentUser

        if (user == null) {
            close(IllegalStateException("Please sign in again."))
            return@callbackFlow
        }

        var registration: ListenerRegistration? = null

        registration = propertiesCollection
            .whereEqualTo("ownerUid", user.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val listings = snapshot?.documents
                    ?.mapNotNull { document ->
                        document.toObject(PropertyListing::class.java)
                            ?.copy(id = document.id)
                    }
                    ?.sortedByDescending {
                        it.createdAt?.toDate()?.time ?: 0L
                    }
                    .orEmpty()

                trySend(listings)
            }

        awaitClose {
            registration?.remove()
        }
    }
}