package com.splitstay.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.splitstay.app.RoleSelectionScreen
import com.splitstay.app.SplitStaySplashScreen
import com.splitstay.app.admin.presentation.AdminReviewDashboardScreen
import com.splitstay.app.auth.presentation.AuthUiState
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.auth.presentation.AuthViewModelFactory
import com.splitstay.app.auth.presentation.EmailVerificationScreen
import com.splitstay.app.auth.presentation.OwnerKycScreen
import com.splitstay.app.kyc.data.LocalKycRepository
import com.splitstay.app.kyc.presentation.KycViewModel
import com.splitstay.app.kyc.presentation.KycViewModelFactory
import com.splitstay.app.owner.presentation.OwnerApplicationStatusScreen
import com.splitstay.app.property.data.PropertyRepository
import com.splitstay.app.property.presentation.PropertyViewModel
import com.splitstay.app.property.presentation.PropertyViewModelFactory

@Composable
fun AppNavigation(
    internetAvailable: Boolean
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val kycViewModel: KycViewModel = viewModel(
        factory = KycViewModelFactory(
            LocalKycRepository(context.applicationContext)
        )
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory()
    )

    val propertyRepository = PropertyRepository(
        firestore = FirebaseFirestore.getInstance(),
        firebaseAuth = FirebaseAuth.getInstance()
    )

    val propertyViewModel: PropertyViewModel = viewModel(
        factory = PropertyViewModelFactory(propertyRepository)
    )

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
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

        composable<AdminDashboard> {
            val application by
            kycViewModel.application.collectAsState()

            AdminReviewDashboardScreen(
                application = application,
                onApprove = {
                    kycViewModel.updateDemoStatus("APPROVED")
                },
                onReject = { reason ->
                    kycViewModel.updateDemoStatus(
                        status = "REJECTED",
                        reason = reason
                    )
                }
            )
        }

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

        composable<EmailVerification> {
            val uiState by
            authViewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                when (uiState) {
                    AuthUiState.StudentProfileReady -> {
                        navController.navigate(StudentHome) {
                            popUpTo<AuthGraph> {
                                inclusive = true
                            }
                        }
                    }

                    AuthUiState.OwnerKycRequired -> {
                        navController.navigate(OwnerKycRequired) {
                            popUpTo<AuthGraph> {
                                inclusive = true
                            }
                        }
                    }

                    else -> Unit
                }
            }

            EmailVerificationScreen(
                email = authViewModel.currentUserEmail ?: "",
                isVerified = uiState is AuthUiState.EmailVerified,
                isLoading = uiState is AuthUiState.Loading,
                errorMessage = (uiState as? AuthUiState.Error)?.message,
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

        composable<OwnerKycRequired> {
            OwnerKycScreen(
                kycViewModel = kycViewModel,
                onSubmitKyc = {
                    kycViewModel.submitForDemoReview(
                        kycViewModel.application.value
                    )

                    navController.navigate(OwnerPendingApproval) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<OwnerPendingApproval> {
            val application by
            kycViewModel.application.collectAsState()

            OwnerApplicationStatusScreen(
                application = application,
                onOpenAdminDemo = {
                    navController.navigate(AdminDashboard) {
                        launchSingleTop = true
                    }
                }
            )
        }

        studentNavGraph(
            authViewModel = authViewModel
        )

        ownerNavGraph(
            navController = navController,
            authViewModel = authViewModel,
            propertyViewModel = propertyViewModel
        )
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

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}