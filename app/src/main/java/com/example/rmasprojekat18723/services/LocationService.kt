package com.example.rmasprojekat18723.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rmasprojekat18723.MainActivity
import com.example.rmasprojekat18723.R
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore

class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val db = FirebaseFirestore.getInstance()

    private var lastNotifiedUserId: String? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    checkNearbyObjects(location)
                }
            }
        }
        startForegroundService()
        startLocationUpdates()
    }

    private fun startForegroundService() {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("location_service", "Location and Notification Service")
        } else {
            "default_channel"
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking Location")
            .setContentText("Your location is being tracked, and you'll receive notifications about nearby users.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }
        return channelId
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun checkNearbyObjects(location: Location) {
        db.collection("objects").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val lat = document.getDouble("latitude")
                    val lon = document.getDouble("longitude")
                    val objectId = document.id
                    val objectTitle = document.getString("title")

                    if (lat != null && lon != null && objectId != lastNotifiedUserId) {
                        val objectLocation = Location("").apply {
                            latitude = lat
                            longitude = lon
                        }

                        val distance = location.distanceTo(objectLocation)
                        if (distance < 100) {
                            sendNotification(objectId, "Nearby Object", "$objectTitle is near your location.")
                            lastNotifiedUserId = objectId
                        }
                    }
                }
            }
    }

    private fun sendNotification(userId: String, title: String, content: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "location_service")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(userId.hashCode(), notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun updateUserLocationInFirestore(location: Location) {
        val userId = "your_user_id" // zameni sa stvarnim userId
        val userLocation = mapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude
        )

        db.collection("users").document(userId)
            .set(userLocation)
            .addOnSuccessListener {
                Log.d("LocationService", "User location updated in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.w("LocationService", "Error updating location", e)
            }
    }
}