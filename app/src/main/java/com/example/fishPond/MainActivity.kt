package com.example.fishPond

import android.app.Application
import com.google.firebase.FirebaseApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import com.example.fishPond.ui.theme.FishPondTheme
import com.example.mymsat.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize firebase
        Firebase.database.setPersistenceEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_status_bar_color) // Replace 'my_status_bar_color' with your color resource
        // This enables edge-to-edge layout, making the app's content go behind the status bar
        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FishPondTheme {
                MainScreen()
            }
        }
    }
}
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
