package com.samplural.fivecrownsscorekeeper.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samplural.fivecrownsscorekeeper.ui.screens.AboutScreen
import com.samplural.fivecrownsscorekeeper.ui.screens.HomeApp
import com.samplural.fivecrownsscorekeeper.ui.screens.SettingsScreen
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
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("about") {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
    }

}