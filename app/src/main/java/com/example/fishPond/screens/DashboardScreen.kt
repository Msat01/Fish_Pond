package com.example.fishPond.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fishPond.modules.CustomTopAppBar
import com.example.fishPond.modules.DashboardBottomBar
import com.example.fishPond.repository.SensorDataRepository
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun DashboardScreen(
    navController: NavController,
    onAddPond: (String) -> Unit,
    onPondClick: (String) -> Unit
) {

    var ponds by remember { mutableStateOf<List<String>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val repository = SensorDataRepository()
    val coroutineScope = rememberCoroutineScope()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    // Fetch the list of ponds from Firebase when the screen is loaded
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            ponds = repository.getPonds()
        }
    }

    fun refreshPonds() {
        coroutineScope.launch {
            isRefreshing = true
                ponds = repository.getPonds() // Re-fetch the list of ponds
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Available Fish Ponds"
            )
        },
        bottomBar = { DashboardBottomBar(navController = navController) },
        //scaffoldState = scaffoldState
    ) { innerPadding ->
        // Your screen content goes here



        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { refreshPonds() } // Refresh data when pulled down
        ) {
            // Display the list of ponds
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()), // Set the background color for the top app bar
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                ponds.forEach { pond ->
                    PondListItem(pondName = pond, onPondClick = onPondClick)
                }

                Button(
                    onClick = { onAddPond("New Pond") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Add Pond",
                        color = MaterialTheme.colorScheme.onPrimary,// Text color set to onPrimary
                    )
                }
            }
        }
    }
}
@Composable
fun PondListItem(pondName: String, onPondClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPondClick(pondName) }
            .padding(vertical = 1.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Background color for the card
        shape = MaterialTheme.shapes.medium // Rounded corners for the card
    ) {
        Text(
            text = pondName,
            color = MaterialTheme.colorScheme.onSurface ,// Text color set to onPrimary
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(24.dp)

        )
    }
}
