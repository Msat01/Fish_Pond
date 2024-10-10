package com.example.fishPond

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fishPond.screens.ControlPanelScreen
import com.example.fishPond.screens.DashboardScreen
import com.example.fishPond.screens.NotificationsScreen
import com.example.fishPond.screens.PondSensorDataScreen
import com.example.fishPond.screens.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard",
        //modifier = Modifier.padding(16.dp) // Apply inner padding from Scaffold
    ) {
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                onAddPond = { newPond ->
                    // Update your list of ponds (you'll likely need state handling here)
                },
                onPondClick = { pondId ->
                    navController.navigate("sensor_data/$pondId")
                }
            )
        }
        composable("sensor_data/{pondId}") { backStackEntry ->
            val pondId = backStackEntry.arguments?.getString("pondId") ?: ""
            PondSensorDataScreen(navController, pondId)
        }
        composable("control_panel/{pondId}") { backStackEntry ->
            val pondId = backStackEntry.arguments?.getString("pondId") ?: ""
            ControlPanelScreen(navController, pondId)
        }
        composable("settings/{pondId}") { backStackEntry ->
            val pondId = backStackEntry.arguments?.getString("pondId") ?: ""
            SettingsScreen(navController, pondId)
        }
        composable("notifications") {
            NotificationsScreen(navController = navController) // Pass the actual instance of navController here
        }
    }
}
