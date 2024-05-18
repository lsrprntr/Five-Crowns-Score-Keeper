package com.samplural.fivecrownsscorekeeper.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samplural.fivecrownsscorekeeper.ui.screens.HomeApp
import com.samplural.fivecrownsscorekeeper.ui.screens.SettingsScreen

enum class Screens {
    HOME,
    SETTINGS
}
@Composable
fun NavApp(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeApp(
                onSettingsClick = { navController.navigate("settings") },
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        // Add more destinations similarly.
    }

}