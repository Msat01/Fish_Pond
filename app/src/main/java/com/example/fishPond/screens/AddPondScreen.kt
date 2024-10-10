package com.example.fishPond.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPondScreen(onAddPond: (String) -> Unit) {
    var pondName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Add Pond",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = pondName,
            onValueChange = { pondName = it },
            label = { Text(text = "Pond Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (pondName.isNotEmpty()) {
                    onAddPond(pondName)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add")
        }
    }
}
