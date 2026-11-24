
package com.splitstay.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.student.presentation.StudentDashboardScreen

fun NavGraphBuilder.studentNavGraph() {
    navigation<StudentGraph>(
        startDestination = StudentHome
    ) {
        composable<StudentHome> {
            StudentDashboardScreen()
        }
    }
}
