package com.example.rmasprojekat18723.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rmasprojekat18723.Components.rememberMapViewWithLifecycle
import com.example.rmasprojekat18723.data.MapUIEvent
import com.example.rmasprojekat18723.data.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.rmasprojekat18723.data.ObjectUIEvent
import com.example.rmasprojekat18723.data.ObjectViewModel
import com.google.android.gms.maps.GoogleMap


@Composable
fun MapScreen(mapViewModel: MapViewModel = viewModel(), onSuccess: () -> Unit) {
    val context = LocalContext.current
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val mapState = mapViewModel.mapUIState.value
    var isCameraMovedManually by remember { mutableStateOf(false) }
    var showAddObjectDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mapViewModel.onEvent(MapUIEvent.LoadMarkers)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (hasLocationPermission) {
            val mapView = rememberMapViewWithLifecycle()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize(),
                    update = { map ->
                        map.getMapAsync { googleMap ->
                            googleMap.isMyLocationEnabled = true

                            googleMap.setOnMyLocationChangeListener { location ->
                                val latLng = LatLng(location.latitude, location.longitude)
                                mapViewModel.onEvent(MapUIEvent.LocationUpdate(latLng))
                                if (!isCameraMovedManually) {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                }
                            }

                            googleMap.setOnCameraMoveStartedListener {
                                if (it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                                    isCameraMovedManually = true
                                }
                            }

                            if (mapState.mapMarkers.isNotEmpty()) {
                                mapState.mapMarkers.forEach { markerLocation ->
                                    googleMap.addMarker(
                                        MarkerOptions()
                                            .position(markerLocation)
                                            .title("Marker at: ${markerLocation.latitude}, ${markerLocation.longitude}")
                                    )
                                    Log.d("MapScreen", "Marker added at: ${markerLocation.latitude}, ${markerLocation.longitude}")
                                }
                            } else {
                                Log.e("MapScreen", "No markers to add on the map.")
                            }
                        }
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Location permission is not granted.")
            }
        }

        mapState.mapError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { showAddObjectDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Add Object")
            }
        }

        if (showAddObjectDialog) {
            AddObjectDialog(
                currentLocation = mapViewModel.mapUIState.value.currentLocation,
                onDismiss = { showAddObjectDialog = false },
                onSuccess = {
                    showAddObjectDialog = false
                    mapViewModel.onEvent(MapUIEvent.LoadMarkers)
                    onSuccess()
                }
            )
        }
    }


}


@Composable
fun AddObjectDialog(
    currentLocation: LatLng?,
    objectViewModel: ObjectViewModel = viewModel(),
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val objectState by objectViewModel.objectUIState
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        objectViewModel.onEvent(ObjectUIEvent.PhotoSelected(uri))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Object",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = objectState.title,
                    onValueChange = { objectViewModel.onEvent(ObjectUIEvent.TitleChange(it)) },
                    label = { Text("Title") },
                    isError = objectState.titleError != null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = objectState.description,
                    onValueChange = { objectViewModel.onEvent(ObjectUIEvent.DescriptionChange(it)) },
                    label = { Text("Description") },
                    isError = objectState.descriptionError != null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = objectState.startTime,
                    onValueChange = { objectViewModel.onEvent(ObjectUIEvent.StartTimeChange(it)) },
                    label = { Text("Start Time (Example: 16:45)") },
                    isError = objectState.startTimeError != null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = objectState.duration,
                    onValueChange = { objectViewModel.onEvent(ObjectUIEvent.DurationChange(it)) },
                    label = { Text("Duration (hours,minutes)") },
                    isError = objectState.durationError != null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Choose Photo")
                }

                selectedImageUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .size(128.dp)
                            .padding(vertical = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    objectViewModel.onEvent(ObjectUIEvent.AddObjectClicked(onSuccess,currentLocation))
                }
            ) {
                Text(text = "Add Object")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}


