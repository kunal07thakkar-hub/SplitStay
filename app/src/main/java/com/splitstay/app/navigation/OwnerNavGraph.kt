
package com.splitstay.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.splitstay.app.owner.presentation.OwnerDashboardScreen

fun NavGraphBuilder.ownerNavGraph() {
    navigation<OwnerGraph>(
        startDestination = OwnerHome
    ) {
        composable<OwnerHome> {
            OwnerDashboardScreen()
        }
    }
}
