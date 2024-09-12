package com.example.rmasprojekat18723.Screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rmasprojekat18723.Components.rememberMapViewWithLifecycle
import com.example.rmasprojekat18723.data.MapUIEvent
import com.example.rmasprojekat18723.data.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


@Composable
fun MapScreen(mapViewModel: MapViewModel = viewModel()) {
    val context = LocalContext.current
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val mapState = mapViewModel.mapUIState.value

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
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            }


                            mapState.mapMarkers.forEach { markerLocation ->
                                googleMap.addMarker(MarkerOptions().position(markerLocation))
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
    }
}