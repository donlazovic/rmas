package com.example.rmasprojekat18723.Screens

import android.Manifest
import android.util.Log
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rmasprojekat18723.data.SignUpUIEvent
import com.example.rmasprojekat18723.data.SignUpViewModel
import com.example.rmasprojekat18723.services.LocationService
import com.example.rmasprojekat18723.ui.theme.ButtonColor1
import com.example.rmasprojekat18723.ui.theme.ButtonColor2
import com.example.rmasprojekat18723.ui.theme.ButtonColor3

const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001
const val REQUEST_CODE_LOCATION_PERMISSION = 1002

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(signOut: () -> Unit, mapClick: () -> Unit,  addObjectClick: () -> Unit,signupViewModel: SignUpViewModel = viewModel()) {
    val context = LocalContext.current
    var isServiceRunning by rememberSaveable { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "HomeScreen",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    mapClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Show Map",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    addObjectClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add Object",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    checkNotificationPermissionAndStartService(context, isServiceRunning) { isRunning ->
                        isServiceRunning = isRunning
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                if (isServiceRunning) {
                                    listOf(Color.Red, ButtonColor3)
                                } else {
                                    listOf(ButtonColor1, ButtonColor2)
                                }
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isServiceRunning) "Stop Location Service" else "Start Location Service",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { signupViewModel.onEvent(SignUpUIEvent.SignOutClicked(signOut)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }

}


fun checkNotificationPermissionAndStartService(
    context: Context,
    isServiceRunning: Boolean,
    onServiceStatusChanged: (Boolean) -> Unit
) {
    Log.d("ServiceCheck", "Checking permissions")

    val locationPermissionGranted = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val notificationPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    if (!locationPermissionGranted || !notificationPermissionGranted) {
        Log.d("ServiceCheck", "Permissions missing: location = $locationPermissionGranted, notifications = $notificationPermissionGranted")
        if (!locationPermissionGranted) {
            Log.d("ServiceCheck", "Requesting location permission")
            requestLocationPermission(context as Activity)
        }
        if (!notificationPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("ServiceCheck", "Requesting notification permission")
            requestNotificationPermission(context as Activity)
        }

        onServiceStatusChanged(false)
    } else {
        Log.d("ServiceCheck", "All permissions granted. Starting service.")
        toggleLocationService(context, isServiceRunning, onServiceStatusChanged)
    }
}

fun requestLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_CODE_LOCATION_PERMISSION
    )
}

fun requestNotificationPermission(activity: Activity) {
    Log.d("PermissionCheck", "Requesting notification permission")
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        REQUEST_CODE_NOTIFICATION_PERMISSION
    )
}

private fun toggleLocationService(context: Context, isServiceRunning: Boolean, onServiceStatusChanged: (Boolean) -> Unit) {
    if (isServiceRunning) {
        Log.d("ServiceCheck", "Stopping service")
        stopLocationService(context)
        onServiceStatusChanged(false)
    } else {
        Log.d("ServiceCheck", "Starting service")
        startLocationService(context)
        onServiceStatusChanged(true)
    }
}

private fun startLocationService(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    context.startService(intent)
}

private fun stopLocationService(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    context.stopService(intent)
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen({},{},{})
}