
package com.splitstay.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.auth.presentation.StudentAuthScreen
import com.splitstay.app.auth.presentation.OwnerAuthScreen

fun NavGraphBuilder.authNavGraph() {
    navigation<AuthGraph>(
        startDestination = StudentAuth
    ) {
        composable<StudentAuth> {
            StudentAuthScreen()
        }

        composable<OwnerAuth> {
            OwnerAuthScreen()
        }
    }
}
