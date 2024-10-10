package com.example.fishPond.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fishPond.modules.CustomTopAppBar
import com.example.fishPond.repository.SensorDataRepository

@Composable
fun SettingsScreen(navController: NavController, pondId: String) {
    var pondName by remember { mutableStateOf(pondId) }
    val repository = SensorDataRepository()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Settings for $pondId\""
            )
        },
        //scaffoldState = scaffoldState
    ) { innerPadding ->
        // Your screen content goes here

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = pondName,
                onValueChange = { pondName = it },
                label = { Text("Pond Name") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    repository.updatePondName(pondId, pondName)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    repository.deletePond(pondId)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "Delete Pond",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
