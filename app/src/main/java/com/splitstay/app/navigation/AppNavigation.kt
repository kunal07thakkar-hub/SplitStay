
package com.splitstay.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.splitstay.app.navigation.ownerNavGraph
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.splitstay.app.RoleSelectionScreen
import com.splitstay.app.SplitStaySplashScreen

@Composable
fun AppNavigation(
    internetAvailable: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        // Splash screen
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

        // Choose Student or Property Owner
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

        // Separate navigation graphs
        authNavGraph()
        studentNavGraph()
        ownerNavGraph()
    }
}
