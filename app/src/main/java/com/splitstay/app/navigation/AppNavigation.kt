
package com.splitstay.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.splitstay.app.RoleSelectionScreen
import com.splitstay.app.SplitStaySplashScreen
import com.splitstay.app.auth.presentation.AuthUiState
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.auth.presentation.AuthViewModelFactory
import com.splitstay.app.auth.presentation.EmailVerificationScreen

@Composable
fun AppNavigation(
    internetAvailable: Boolean
) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory()
    )

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {

        // Splash
        composable<Splash> {
            SplitStaySplashScreen(
                internetAvailable = internetAvailable,
                onContinue = {
                    navController.navigate(RoleSelection) {
                        popUpTo<Splash> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Role selection
        composable<RoleSelection> {
            RoleSelectionScreen(
                onStudentClick = {
                    navController.navigate(StudentAuth)
                },
                onOwnerClick = {
                    navController.navigate(OwnerAuth)
                }
            )
        }

        // Authentication
        authNavGraph(
            authViewModel = authViewModel,

            onRegistrationNeedsVerification = {
                navController.navigate(EmailVerification) {
                    launchSingleTop = true
                }
            },

            onStudentReady = {
                navController.navigate(StudentHome) {
                    popUpTo<AuthGraph> {
                        inclusive = true
                    }
                }
            },

            onOwnerKycRequired = {
                navController.navigate(OwnerKycRequired) {
                    popUpTo<AuthGraph> {
                        inclusive = true
                    }
                }
            },

            onOwnerPendingApproval = {
                navController.navigate(OwnerPendingApproval) {
                    popUpTo<AuthGraph> {
                        inclusive = true
                    }
                }
            },

            onOwnerReady = {
                navController.navigate(OwnerHome) {
                    popUpTo<AuthGraph> {
                        inclusive = true
                    }
                }
            }
        )

        // Email verification
        composable<EmailVerification> {

            val uiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                when (uiState) {
                    AuthUiState.StudentProfileReady ->
                        navController.navigate(StudentHome) {
                            popUpTo<AuthGraph> {
                                inclusive = true
                            }
                        }

                    AuthUiState.OwnerKycRequired ->
                        navController.navigate(OwnerKycRequired) {
                            popUpTo<AuthGraph> {
                                inclusive = true
                            }
                        }

                    else -> Unit
                }
            }

            EmailVerificationScreen(
                email = authViewModel.currentUserEmail ?: "",

                isVerified =
                    uiState is AuthUiState.EmailVerified,

                isLoading =
                    uiState is AuthUiState.Loading,

                errorMessage =
                    (uiState as? AuthUiState.Error)?.message,

                onResendEmail = {
                    authViewModel.resendVerificationEmail()
                },

                onCheckVerification = {
                    authViewModel.checkEmailVerification()
                },

                onContinue = {
                    if (uiState is AuthUiState.EmailVerified) {
                        authViewModel.completeEmailVerification()
                    }
                }
            )
        }

        // Owner KYC is the next onboarding step.
        composable<OwnerKycRequired> {
            OwnerStatusScreen(
                title = "Complete Owner Verification",
                message = "Your email is verified. Please complete your property and identity verification before your property can be published."
            )
        }

        // Owner approval waiting screen.
        composable<OwnerPendingApproval> {
            OwnerStatusScreen(
                title = "Application Under Review",
                message = "Your owner application is awaiting approval. Property management will become available after approval."
            )
        }

        // Existing dashboard graphs
        studentNavGraph()
        ownerNavGraph()
    }
}

@Composable
private fun OwnerStatusScreen(
    title: String,
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
