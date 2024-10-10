package com.example.fishPond.screens

import android.util.Log
import com.example.fishPond.repository.SensorDataRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fishPond.model.SensorReading
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class) // Suppress the warning for experimental API
@Composable
fun PondSensorDataScreen(navController: NavController, pondId: String) {
    var sensorData by remember { mutableStateOf<SensorReading?>(null) }
    var pondName by remember { mutableStateOf<String?>(null) }
    val repository = SensorDataRepository()
    var expanded by remember { mutableStateOf(false) }

    // Fetching pond name and sensor data when the screen is loaded
    LaunchedEffect(pondId) {
        pondName = repository.getPondName(pondId)
    }

    // Refreshing sensor data every second
    LaunchedEffect(pondId) {
        while (true) {
            sensorData = try {
                repository.getSensorData(pondId)
            } catch (e: Exception) {
                Log.e("Firebase", "Error fetching data", e)
                null
            }
            delay(1000) // Refresh every second
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pond Sensor Data",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium // Use custom headlineMedium

                    )
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
                        offset = DpOffset(x = (-32).dp, y = 0.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Control Panel", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                expanded = false
                                navController.navigate("control_panel/$pondId")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                expanded = false
                                navController.navigate("settings/$pondId")
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary) // Corrected top bar background
                //backgroundColor = MaterialTheme.colorScheme.primary, // Match upper screen color
                //contentColor = MaterialTheme.colorScheme.onPrimary, // Contrast color for text and icons
                //modifier = Modifier.statusBarsPadding() // Pushes content below the status bar
            )
        }
    ) { innerPadding ->
        // Using FlowRow to display sensor data blocks in two columns
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                //.verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Ensure 2 columns per row
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    PondConditionBlock(
                        temperatureComment = sensorData?.temperature?.let { getConditionComment(it, "temperature") } ?: "connection error",
                        phComment = sensorData?.pH?.let { getConditionComment(it, "ph") } ?: "connection error",
                        turbidityComment = sensorData?.turbidity?.let { getConditionComment(it, "turbidity") } ?: "connection error",
                        humidityComment = sensorData?.humidity?.let { getConditionComment(it, "humidity") },
                        waterLevelComment = sensorData?.waterLevel?.let { getConditionComment(it, "waterLevel") }
                    )
                }

                item {
                    SensorDataBlock(
                        label = "Temp",
                        value = sensorData?.temperature?.toString() ?: "null",
                        unit = "°C",
                        comment = sensorData?.temperature?.let {
                            getConditionComment(
                                it,
                                "temperature"
                            )
                        } ?: "error"
                    )
                }
                item {
                    SensorDataBlock(
                        label = "pH Level",
                        value = sensorData?.pH?.toString() ?: "null",
                        unit = "",
                        comment = sensorData?.pH?.let { getConditionComment(it, "ph") }
                            ?: "error"
                    )
                }
                item {
                    SensorDataBlock(
                        label = "Turbidity",
                        value = sensorData?.turbidity?.toString() ?: "null",
                        unit = "NTU",
                        comment = sensorData?.turbidity?.let {
                            getConditionComment(
                                it,
                                "turbidity"
                            )
                        }
                            ?: "error"
                    )
                }
                item {
                    SensorDataBlock(
                        label = "Humidity",
                        value = sensorData?.humidity?.toString() ?: "null",
                        unit = "",
                        comment = sensorData?.humidity?.let { getConditionComment(it, "humidity") }
                            ?: "error"
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    SensorDataBlock(
                        label = "Water Level",
                        value = sensorData?.waterLevel?.toString() ?: "null",
                        unit = "cm",
                        comment = sensorData?.waterLevel?.let {
                            getConditionComment(
                                it,
                                "waterLevel"
                            )
                        }
                            ?: "error"
                    )
                }
            }
        }
    }
}



@Composable
fun SensorDataBlock(label: String, value: String, unit: String, comment: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Set equal height for all blocks
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium // Shape with rounded corners
            )
            .clip(MaterialTheme.shapes.medium) // Clip content to the shape (rounded corners)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
        Text(
            text = "$value $unit",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
        Text(
            text = "Comment: $comment",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


fun getConditionComment(value: Double?, type: String): String {
    if (value == null) return "error"
    return when (type) {
        "temperature" -> when {
            value < 24 -> "low"
            value in 24.0..30.0 -> "normal"
            value > 30 -> "high"
            else -> "high"
        }
        "ph" -> when (value) {
            in 0.0..6.5 -> "acidic"
            in 6.5..8.0 -> "neutral"
            in 8.0..14.0 -> "basic"
            else -> "error"
        }
        "turbidity" -> when {
            value < 25 -> "normal"
            value in 25.0..30.0 -> "high"
            value > 30.0 -> "higher"
            else -> "error"
        }
        "humidity" -> when {
            value < 1 -> "low"
            value in 1.0..5.0 -> "normal"
            value > 5.0 -> "high"
            else -> "normal"
        }
        "waterLevel" -> when {
            value < 1 -> "low"
            value in 1.0..5.0 -> "normal"
            value > 5.0 -> "high"
            else -> "normal"
        }
        else -> "unknown"
    }
}

@Composable
fun PondConditionBlock(
    temperatureComment: String,
    phComment: String,
    turbidityComment: String,
    humidityComment: String?,
    waterLevelComment: String?
) {
    val isGoodCondition = listOf(
        temperatureComment, turbidityComment, humidityComment, waterLevelComment
    ).all { it == "normal" } && phComment == "neutral"

    val isErrorCondition = listOf(
        temperatureComment, turbidityComment, humidityComment, waterLevelComment, phComment
    ).all { it == "error" }

    val condition = if (isGoodCondition) {
        "GOOD"
    } else if (isErrorCondition) {
        "Error"
    } else {
        "ACTION NEEDED"
    }

    val action = if (isGoodCondition) {
        "Your pond is in good condition."
    } else if (isErrorCondition) {
        "Loading..."
    }
        else  {
            determineAction(
                temperatureComment,
                phComment,
                turbidityComment,
                humidityComment,
                waterLevelComment
            )
        }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Set equal height for all blocks
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium // Shape with rounded corners
            )
            .clip(MaterialTheme.shapes.medium) // Clip content to the shape (rounded corners)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Pond Condition",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
        Text(
            text = "Condition: $condition",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
        Text(
            text = "Action: $action",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

fun determineAction(
    temperatureComment: String,
    phComment: String,
    turbidityComment: String,
    humidityComment: String?,
    waterLevelComment: String?
): String {
    return buildString {
        if (temperatureComment != "normal") {
            if (temperatureComment == "low") {
                append("Activate heater please ")
            }
            if (temperatureComment == "high") {
                append("Activate cooler please ")
            }
        }
        if (phComment != "neutral") {
            append("change the water partially to Balance pH levels. ")
        }
        if (turbidityComment != "normal") {
            if (turbidityComment == "high") {
                append("You'll soon need to change the pond water. ")
            }
            if (turbidityComment == "high") {
                append("Change the pond water now! ")
            }
        }
        if (humidityComment != "normal") {
            append("Check humidity levels. ")
        }
        if (waterLevelComment != "normal") {
            if (waterLevelComment == "low") {
                append("Increase water level. ")
            }
            if (waterLevelComment == "high") {
                append("Decrease water level. ")
            }
        }
    }.ifBlank { "No specific action required." }
}
