package com.hacksolotls.tracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Define your navigation graph
    NavHost(navController = navController, startDestination = "home") {
        composable("calculator") {
            CalculatorScreen(navController)  // Pass the NavController to CalculatorScreen
        }
        composable("home") {
            MainScreen(navController=navController)  // Define the "home" screen
        }
        composable("calendar") {
            CalendarScreen(navController=navController)  // Define the "home" screen
        }
        composable("settings") {
            SettingsScreen(navController=navController)  // Define the "home" screen
        }
    }
}
