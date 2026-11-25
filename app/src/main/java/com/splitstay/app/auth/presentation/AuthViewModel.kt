
package com.splitstay.app.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitstay.app.auth.data.FirebaseAuthRepository
import com.splitstay.app.data.remote.FirestoreUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class AccountRole {
    STUDENT,
    OWNER
}

data class RegistrationDraft(
    val role: AccountRole,
    val fullName: String,
    val email: String,
    val mobileNumber: String,
    val propertyName: String? = null
)

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object VerificationEmailSent : AuthUiState
    data object EmailVerificationRequired : AuthUiState
    data object EmailVerified : AuthUiState
    data object StudentProfileReady : AuthUiState
    data object OwnerKycRequired : AuthUiState
    data object OwnerPendingApproval : AuthUiState
    data object OwnerProfileReady : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val repository: FirebaseAuthRepository =
        FirebaseAuthRepository(),
    private val userRepository: FirestoreUserRepository =
        FirestoreUserRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    private val _registrationDraft =
        MutableStateFlow<RegistrationDraft?>(null)

    val registrationDraft: StateFlow<RegistrationDraft?> =
        _registrationDraft.asStateFlow()

    val currentUserEmail: String?
        get() = repository.currentUser?.email

    // -----------------------------------------
    // STUDENT REGISTRATION
    // -----------------------------------------

    fun registerStudent(
        fullName: String,
        email: String,
        mobile: String,
        password: String
    ) {
        register(
            role = AccountRole.STUDENT,
            fullName = fullName,
            email = email,
            mobile = mobile,
            password = password
        )
    }

    // -----------------------------------------
    // OWNER REGISTRATION
    // -----------------------------------------

    fun registerOwner(
        fullName: String,
        propertyName: String,
        email: String,
        mobile: String,
        password: String
    ) {
        register(
            role = AccountRole.OWNER,
            fullName = fullName,
            email = email,
            mobile = mobile,
            password = password,
            propertyName = propertyName
        )
    }

    // -----------------------------------------
    // COMMON REGISTRATION
    // -----------------------------------------

    private fun register(
        role: AccountRole,
        fullName: String,
        email: String,
        mobile: String,
        password: String,
        propertyName: String? = null
    ) {
        if (
            fullName.isBlank() ||
            email.isBlank() ||
            mobile.isBlank() ||
            password.isBlank() ||
            (role == AccountRole.OWNER && propertyName.isNullOrBlank())
        ) {
            _uiState.value = AuthUiState.Error(
                "Please fill in all required fields."
            )
            return
        }

        val normalizedMobile = normalizePhoneNumber(mobile)

        if (normalizedMobile == null) {
            _uiState.value = AuthUiState.Error(
                "Please enter a valid mobile number."
            )
            return
        }

        val draft = RegistrationDraft(
            role = role,
            fullName = fullName.trim(),
            email = email.trim(),
            mobileNumber = normalizedMobile,
            propertyName = propertyName?.trim()
        )

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                repository.registerWithEmail(
                    fullName = draft.fullName,
                    email = draft.email,
                    password = password
                )

                _registrationDraft.value = draft

                _uiState.value =
                    AuthUiState.VerificationEmailSent

            } catch (exception: Exception) {
                _uiState.value = AuthUiState.Error(
                    exception.localizedMessage
                        ?: "Registration failed. Please try again."
                )
            }
        }
    }

    // -----------------------------------------
    // LOGIN
    // -----------------------------------------

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error(
                "Please enter your email and password."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                val user = repository.loginWithEmail(
                    email = email.trim(),
                    password = password
                )

                user.reload()

                val refreshedUser = repository.currentUser
                    ?: throw IllegalStateException(
                        "Please sign in again."
                    )

                if (!refreshedUser.isEmailVerified) {
                    _uiState.value =
                        AuthUiState.EmailVerificationRequired
                    return@launch
                }

                refreshedUser.getIdToken(true).await()

                val profile = userRepository.getUserProfile(
                    refreshedUser.uid
                )

                if (profile == null) {
                    _uiState.value = AuthUiState.Error(
                        "Your profile is not completed. Please contact support."
                    )
                    return@launch
                }

                _uiState.value = when {
                    profile.role == "STUDENT" &&
                            profile.accountStatus == "ACTIVE" ->
                        AuthUiState.StudentProfileReady

                    profile.role == "OWNER" &&
                            profile.accountStatus == "PENDING_KYC" ->
                        AuthUiState.OwnerKycRequired

                    profile.role == "OWNER" &&
                            profile.accountStatus == "PENDING_APPROVAL" ->
                        AuthUiState.OwnerPendingApproval

                    profile.role == "OWNER" &&
                            profile.accountStatus == "APPROVED" ->
                        AuthUiState.OwnerProfileReady

                    else -> AuthUiState.Error(
                        "Your account is not currently active."
                    )
                }

            } catch (exception: Exception) {
                _uiState.value = AuthUiState.Error(
                    exception.localizedMessage
                        ?: "Login failed. Please try again."
                )
            }
        }
    }

    // -----------------------------------------
    // EMAIL VERIFICATION
    // -----------------------------------------

    fun checkEmailVerification() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                val user = repository.refreshCurrentUser()

                when {
                    user == null ->
                        _uiState.value = AuthUiState.Error(
                            "Please sign in again."
                        )

                    !user.isEmailVerified ->
                        _uiState.value =
                            AuthUiState.EmailVerificationRequired

                    else -> {
                        user.getIdToken(true).await()
                        _uiState.value = AuthUiState.EmailVerified
                    }
                }

            } catch (exception: Exception) {
                _uiState.value = AuthUiState.Error(
                    exception.localizedMessage
                        ?: "Could not check email verification."
                )
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                repository.resendEmailVerification()

                _uiState.value =
                    AuthUiState.VerificationEmailSent

            } catch (exception: Exception) {
                _uiState.value = AuthUiState.Error(
                    exception.localizedMessage
                        ?: "Could not send verification email."
                )
            }
        }
    }

    // -----------------------------------------
    // COMPLETE EMAIL-VERIFIED REGISTRATION
    // -----------------------------------------

    fun completeEmailVerification() {
        val draft = _registrationDraft.value

        if (draft == null) {
            _uiState.value = AuthUiState.Error(
                "Registration details were not found. Please sign in again."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                val user = repository.refreshCurrentUser()
                    ?: throw IllegalStateException(
                        "Please sign in again."
                    )

                if (!user.isEmailVerified) {
                    _uiState.value =
                        AuthUiState.EmailVerificationRequired
                    return@launch
                }

                user.getIdToken(true).await()

                userRepository.createUserProfile(
                    uid = user.uid,
                    fullName = draft.fullName,
                    mobileNumber = draft.mobileNumber,
                    role = draft.role.name,
                    propertyName = draft.propertyName.orEmpty()
                )
                _uiState.value = when (draft.role) {
                    AccountRole.STUDENT ->
                        AuthUiState.StudentProfileReady

                    AccountRole.OWNER ->
                        AuthUiState.OwnerKycRequired
                }

                _registrationDraft.value = null

            } catch (exception: Exception) {
                _uiState.value = AuthUiState.Error(
                    exception.localizedMessage
                        ?: "Could not create your profile."
                )
            }
        }
    }

    // -----------------------------------------
    // PHONE NUMBER NORMALIZATION
    // -----------------------------------------

    private fun normalizePhoneNumber(
        mobile: String
    ): String? {

        val cleaned = mobile.trim().replace(
            Regex("[\\s()-]"),
            ""
        )

        val normalized = when {
            cleaned.startsWith("+") -> cleaned

            cleaned.length == 10 &&
                    cleaned.all(Char::isDigit) ->
                "+91$cleaned"

            else -> return null
        }

        return normalized.takeIf {
            it.matches(Regex("^\\+[1-9][0-9]{7,14}$"))
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
