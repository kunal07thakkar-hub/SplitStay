package com.splitstay.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.owner.presentation.OwnerAddPropertyScreen
import com.splitstay.app.owner.presentation.OwnerDashboardScreen
import com.splitstay.app.owner.presentation.OwnerMyPropertiesScreen
import com.splitstay.app.property.presentation.PropertyViewModel

fun NavGraphBuilder.ownerNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    propertyViewModel: PropertyViewModel
) {
    navigation<OwnerGraph>(startDestination = OwnerHome) {

        composable<OwnerHome> {
            val profile by authViewModel.ownerProfile.collectAsState()
            val isProfileLoading by
            authViewModel.isOwnerProfileLoading.collectAsState()
            val profileError by
            authViewModel.ownerProfileError.collectAsState()
            val properties by propertyViewModel.properties.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.loadOwnerProfile()
                propertyViewModel.observeProperties()
            }

            OwnerDashboardScreen(
                ownerName = profile?.fullName ?: "Property Owner",
                propertyName = profile?.propertyName.orEmpty(),
                propertyCount = properties.size,
                isLoading = isProfileLoading,
                errorMessage = profileError,
                onRetry = {
                    authViewModel.loadOwnerProfile()
                },
                onPropertiesClick = {
                    navController.navigate(OwnerProperties)
                }
            )
        }

        composable<OwnerProperties> {
            val properties by
            propertyViewModel.properties.collectAsState()
            val isLoading by
            propertyViewModel.isLoading.collectAsState()
            val errorMessage by
            propertyViewModel.errorMessage.collectAsState()

            LaunchedEffect(Unit) {
                propertyViewModel.observeProperties()
            }

            OwnerMyPropertiesScreen(
                properties = properties,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRetry = {
                    propertyViewModel.retryLoading()
                },
                onAddProperty = {
                    navController.navigate(OwnerAddProperty)
                }
            )
        }

        composable<OwnerAddProperty> {
            val isSaving by
            propertyViewModel.isSaving.collectAsState()
            val saveError by
            propertyViewModel.saveErrorMessage.collectAsState()
            val saveCompleted by
            propertyViewModel.saveCompleted.collectAsState()

            LaunchedEffect(saveCompleted) {
                if (saveCompleted) {
                    propertyViewModel.clearSaveState()
                    navController.popBackStack()
                }
            }

            OwnerAddPropertyScreen(
                isSaving = isSaving,
                errorMessage = saveError,
                onSaveProperty = { draft ->
                    propertyViewModel.addProperty(draft)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}