
package com.splitstay.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.auth.presentation.AuthViewModel
import com.splitstay.app.student.presentation.StudentDashboardScreen

fun NavGraphBuilder.studentNavGraph(
    authViewModel: AuthViewModel
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
                }
            )
        }
    }
}
