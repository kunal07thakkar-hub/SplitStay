
package com.splitstay.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

import com.splitstay.app.auth.presentation.AuthUiState
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.auth.presentation.StudentAuthScreen
import com.splitstay.app.auth.presentation.OwnerAuthScreen

fun NavGraphBuilder.authNavGraph(
    authViewModel: AuthViewModel,
    onRegistrationNeedsVerification: () -> Unit,
    onStudentReady: () -> Unit,
    onOwnerKycRequired: () -> Unit,
    onOwnerPendingApproval: () -> Unit,
    onOwnerReady: () -> Unit
) {
    navigation<AuthGraph>(
        startDestination = StudentAuth
    ) {

        composable<StudentAuth> {

            val uiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                when (uiState) {
                    AuthUiState.VerificationEmailSent ->
                        onRegistrationNeedsVerification()

                    AuthUiState.StudentProfileReady ->
                        onStudentReady()

                    AuthUiState.OwnerKycRequired ->
                        onOwnerKycRequired()

                    AuthUiState.OwnerPendingApproval ->
                        onOwnerPendingApproval()

                    AuthUiState.OwnerProfileReady ->
                        onOwnerReady()

                    else -> Unit
                }
            }

            StudentAuthScreen(
                isLoading = uiState is AuthUiState.Loading,

                errorMessage =
                    (uiState as? AuthUiState.Error)?.message,

                onSubmit = {
                        fullName, email, mobile, password, isRegister ->

                    if (isRegister) {
                        authViewModel.registerStudent(
                            fullName = fullName,
                            email = email,
                            mobile = mobile,
                            password = password
                        )
                    } else {
                        authViewModel.login(
                            email = email,
                            password = password
                        )
                    }
                }
            )
        }

        composable<OwnerAuth> {

            val uiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                when (uiState) {
                    AuthUiState.VerificationEmailSent ->
                        onRegistrationNeedsVerification()

                    AuthUiState.StudentProfileReady ->
                        onStudentReady()

                    AuthUiState.OwnerKycRequired ->
                        onOwnerKycRequired()

                    AuthUiState.OwnerPendingApproval ->
                        onOwnerPendingApproval()

                    AuthUiState.OwnerProfileReady ->
                        onOwnerReady()

                    else -> Unit
                }
            }

            OwnerAuthScreen(
                isLoading = uiState is AuthUiState.Loading,

                errorMessage =
                    (uiState as? AuthUiState.Error)?.message,

                onSubmit = {
                        fullName, propertyName, email, mobile,
                        password, isRegister ->

                    if (isRegister) {
                        authViewModel.registerOwner(
                            fullName = fullName,
                            propertyName = propertyName,
                            email = email,
                            mobile = mobile,
                            password = password
                        )
                    } else {
                        authViewModel.login(
                            email = email,
                            password = password
                        )
                    }
                }
            )
        }
    }
}
