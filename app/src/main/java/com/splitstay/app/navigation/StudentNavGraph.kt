
package com.splitstay.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.property.presentation.PropertyViewModel
import com.splitstay.app.student.presentation.StudentDashboardScreen
import com.splitstay.app.student.presentation.StudentExploreScreen

fun NavGraphBuilder.studentNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    propertyViewModel: PropertyViewModel
) {
    navigation<StudentGraph>(
        startDestination = StudentHome
    ) {
        composable<StudentHome> {
            val profile by authViewModel.studentProfile.collectAsState()
            val isLoading by authViewModel.isStudentProfileLoading.collectAsState()
            val errorMessage by authViewModel.studentProfileError.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.loadStudentProfile()
            }

            StudentDashboardScreen(
                studentName = profile?.fullName ?: "Student",
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRetry = {
                    authViewModel.loadStudentProfile()
                },
                onSearchClick = {
                    navController.navigate(StudentExplore)
                }
            )
        }

        composable<StudentExplore> {
            val properties by propertyViewModel.studentProperties.collectAsState()
            val isLoading by propertyViewModel.isStudentLoading.collectAsState()
            val errorMessage by propertyViewModel.studentErrorMessage.collectAsState()

            LaunchedEffect(Unit) {
                propertyViewModel.observeStudentProperties()
            }

            StudentExploreScreen(
                properties = properties,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRetry = {
                    propertyViewModel.retryStudentLoading()
                },
                onBack = {
                    navController.popBackStack()
                },
                onPropertyClick = { propertyId ->
                    // Property details navigation will be added in the next milestone.
                }
            )
        }
    }
}