package com.example.fishPond.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.fishPond.modules.CustomTopAppBar
import com.example.fishPond.modules.DashboardBottomBar

@Composable
fun NotificationsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Notifications"
            )
        },
        bottomBar = { DashboardBottomBar(navController = navController) },
        //scaffoldState = scaffoldState
    ) { innerPadding ->
        // Your screen content goes here

        Text(text = "Notifications",
            modifier = Modifier .padding(innerPadding))
    }
}
