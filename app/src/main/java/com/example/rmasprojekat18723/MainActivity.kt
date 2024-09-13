package com.example.rmasprojekat18723

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import com.example.rmasprojekat18723.App.PawPalsApps
import com.example.rmasprojekat18723.ui.theme.RMASProjekat18723Theme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionsResult(permissions)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) {
            requestPermissionsAtStart()
        }

        setContent {
            RMASProjekat18723Theme {
                PawPalsApps()
            }
        }
    }

    private fun requestPermissionsAtStart() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        val locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationPermissionGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val notificationPermissionGranted = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }
        
        if (locationPermissionGranted && coarseLocationPermissionGranted && notificationPermissionGranted) {
            Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
        } else {
            if (!locationPermissionGranted) {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
            if (!notificationPermissionGranted) {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
