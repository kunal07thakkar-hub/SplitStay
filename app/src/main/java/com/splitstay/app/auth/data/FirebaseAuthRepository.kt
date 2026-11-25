
package com.splitstay.app.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    fun sendPhoneVerificationCode(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: (
            String,
            PhoneAuthProvider.ForceResendingToken
        ) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (Exception) -> Unit,
        resendToken: PhoneAuthProvider.ForceResendingToken? = null
    ) {
        val callbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(
                    credential: PhoneAuthCredential
                ) {
                    onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(
                    exception: FirebaseException
                ) {
                    onVerificationFailed(exception)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId, token)
                }
            }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .apply {
                if (resendToken != null) {
                    setForceResendingToken(resendToken)
                }
            }
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun linkPhoneCredential(
        credential: PhoneAuthCredential
    ): FirebaseUser {
        val user = firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Please sign in before verifying your phone number."
            )

        val result = user.linkWithCredential(credential).await()

        val linkedUser = result.user
            ?: throw IllegalStateException(
                "Phone number linking failed."
            )

        linkedUser.reload().await()

        return firebaseAuth.currentUser
            ?: throw IllegalStateException(
                "Could not refresh the linked account."
            )
    }

    fun createPhoneCredential(
        verificationId: String,
        otp: String
    ): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(
            verificationId,
            otp
        )
    }


    suspend fun registerWithEmail(
        fullName: String,
        email: String,
        password: String
    ): FirebaseUser {
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email.trim(), password)
            .await()

        val user = result.user
            ?: throw IllegalStateException("Account creation failed.")

        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName.trim())
            .build()

        user.updateProfile(profileUpdate).await()

        // Send verification email immediately.
        user.sendEmailVerification().await()

        return user
    }

    suspend fun loginWithEmail(
        email: String,
        password: String
    ): FirebaseUser {
        val result = firebaseAuth
            .signInWithEmailAndPassword(email.trim(), password)
            .await()

        return result.user
            ?: throw IllegalStateException("Login failed.")
    }

    suspend fun resendEmailVerification() {
        val user = firebaseAuth.currentUser
            ?: throw IllegalStateException("No signed-in user found.")

        user.sendEmailVerification().await()
    }

    suspend fun refreshCurrentUser(): FirebaseUser? {
        val user = firebaseAuth.currentUser ?: return null
        user.reload().await()
        return firebaseAuth.currentUser
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}
