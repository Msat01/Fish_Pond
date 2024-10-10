package com.example.fishPond.screens

import androidx.compose.foundation.background
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.navigation.NavHostController
import com.example.fishPond.modules.CustomTopAppBar
import kotlinx.coroutines.delay
import kotlin.text.*

@Composable
fun ControlPanelScreen(navController: NavHostController, pondId: String) {
    var isPumpInOn by remember { mutableStateOf(false) }
    var isPumpOutOn by remember { mutableStateOf(false) }
    var isFeederOn by remember { mutableStateOf(false) }
    var isAeratorOn by remember { mutableStateOf(false) }
    var timer by remember { mutableLongStateOf(0L) }
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(isPumpInOn, isPumpOutOn, isFeederOn, isAeratorOn) {
        if (isPumpInOn || isPumpOutOn || isFeederOn || isAeratorOn) {
            val startTime = System.currentTimeMillis()
            while (isPumpInOn || isPumpOutOn || isFeederOn || isAeratorOn) {
                delay(1000)
                timer = (System.currentTimeMillis() - startTime) / 1000
            }
        } else {
            timer = 0L
        }
    }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Control Panel"
                )
        },
        //scaffoldState = scaffoldState
    ) {innerPadding ->
        // Your screen content goes here

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            ControlItem("Pump IN", isPumpInOn) { isPumpInOn = it }
            ControlItem("Pump OUT", isPumpOutOn) { isPumpOutOn = it }
            ControlItem("Feeder", isFeederOn) { isFeederOn = it }
            ControlItem("Aerator", isAeratorOn) { isAeratorOn = it }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Timer: ${
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d:%02d",
                            timer / 3600,
                            (timer % 3600) / 60,
                            timer % 60
                        )
                    }"
                )
                Button(
                    onClick = { timer = 0L },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "Reset",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ControlItem(label: String, isOn: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = isOn, onCheckedChange = onToggle)
    }
}
