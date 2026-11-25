
package com.splitstay.app.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance(),
    private val firebaseAuth: FirebaseAuth =
        FirebaseAuth.getInstance()
) {

    private fun userDocument(uid: String) =
        firestore.collection("users").document(uid)

    suspend fun createUserProfile(
        uid: String,
        fullName: String,
        mobileNumber: String,
        role: String,
        propertyName: String
    ): UserProfile {

        val user = firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Please sign in again."
            )

        require(user.uid == uid) {
            "Account mismatch. Please sign in again."
        }

        user.reload().await()

        val refreshedUser = firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Please sign in again."
            )

        require(refreshedUser.isEmailVerified) {
            "Please verify your email before continuing."
        }

        // Refresh the Firebase ID token.
        refreshedUser.getIdToken(true).await()

        val email = refreshedUser.email
            ?: throw IllegalStateException(
                "Email address not found."
            )

        val status = when (role) {
            "STUDENT" -> "ACTIVE"
            "OWNER" -> "PENDING_KYC"
            else -> throw IllegalArgumentException(
                "Invalid account role."
            )
        }

        val profile = UserProfile(
            uid = uid,
            fullName = fullName,
            email = email,
            phoneNumber = mobileNumber,
            role = role,
            accountStatus = status,
            propertyName = propertyName
        )

        // Create only; do not overwrite an existing profile.
        firestore.runTransaction { transaction ->
            val documentRef = userDocument(uid)
            val existingDocument = transaction.get(documentRef)

            if (existingDocument.exists()) {
                throw IllegalStateException("A profile already exists for this account.")
            }

            transaction.set(documentRef, profile)
            null
        }.await()

        return profile
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        val snapshot = userDocument(uid).get().await()

        return snapshot.toObject(UserProfile::class.java)
    }

    fun observeUserProfile(
        uid: String
    ): Flow<UserProfile?> = callbackFlow {

        val registration = userDocument(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {
                    trySend(null)
                } else {
                    trySend(
                        snapshot.toObject(UserProfile::class.java)
                    )
                }
            }

        awaitClose {
            registration.remove()
        }
    }
}
