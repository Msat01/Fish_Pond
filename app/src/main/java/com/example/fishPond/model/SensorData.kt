package com.example.fishPond.model

data class SensorReading(
    val temperature: Double? = null,
    val pH: Double? = null,
    val turbidity: Double? = null,
    val humidity: Double? = null,
    val waterLevel: Double? = null

)
