
package com.splitstay.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.owner.presentation.OwnerDashboardScreen

fun NavGraphBuilder.ownerNavGraph(
    authViewModel: AuthViewModel
) {
    navigation<OwnerGraph>(
        startDestination = OwnerHome
    ) {
        composable<OwnerHome> {

            val profile by authViewModel.ownerProfile.collectAsState()
            val isLoading by authViewModel.isOwnerProfileLoading.collectAsState()
            val errorMessage by authViewModel.ownerProfileError.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.loadOwnerProfile()
            }

            OwnerDashboardScreen(
                ownerName = profile?.fullName ?: "Property Owner",
                propertyName = profile?.propertyName.orEmpty(),
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRetry = {
                    authViewModel.loadOwnerProfile()
                }
            )
        }
    }
}
