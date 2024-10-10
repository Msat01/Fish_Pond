package com.example.fishPond.repository

import android.util.Log
import com.example.fishPond.model.SensorReading
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SensorDataRepository {

    private val database = FirebaseDatabase.getInstance("https://fish-pond-monitoring-sys-c1369-default-rtdb.firebaseio.com/").reference

    suspend fun getSensorData(pondId: String): SensorReading? {
        return try {
            val dataSnapshot = database.child("ponds/$pondId/sensorData").get().await()
            val temperature = dataSnapshot.child("temperature/value").getValue(Double::class.java)
            val pH = dataSnapshot.child("pH/value").getValue(Double::class.java)
            val turbidity = dataSnapshot.child("turbidity/value").getValue(Double::class.java)
            val humidity = dataSnapshot.child("humidity/value").getValue(Double::class.java)
            val waterLevel = dataSnapshot.child("waterLevel/value").getValue(Double::class.java)
            SensorReading(temperature = temperature, pH = pH, turbidity = turbidity, waterLevel = waterLevel, humidity = humidity)
        } catch (e: Exception) {
            Log.e("Firebase", "Error fetching data", e)
            null
        }
    }
    // Function to fetch the list of ponds
    suspend fun getPonds(): List<String> {
        return try {
            val snapshot = database.child("ponds").get().await()
            snapshot.children.mapNotNull { it.key } // Get the pond IDs (keys)
        } catch (e: Exception) {
            Log.e("Firebase", "Error fetching ponds", e)
            emptyList()
        }
    }

    suspend fun getPondName(pondId: String): String? {
        return try {
            val dataSnapshot = database.child("ponds/$pondId/name").get().await()
            dataSnapshot.getValue(String::class.java)
        } catch (e: Exception) {
            Log.e("Firebase", "Error fetching pond name", e)
            null
        }
    }


    fun updatePondName(pondId: String, newName: String) {
        database.child("ponds").child(pondId).child("name").setValue(newName)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun deletePond(pondId: String) {
        database.child("ponds").child(pondId).removeValue()
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}
