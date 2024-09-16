package com.example.rmasprojekat18723.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

class MapViewModel : ViewModel() {

    var mapUIState = mutableStateOf(MapUIState())


    fun onEvent(event: MapUIEvent) {
        when (event) {
            is MapUIEvent.LocationUpdate -> {
                mapUIState.value = mapUIState.value.copy(
                    currentLocation = event.location
                )
            }
            is MapUIEvent.LoadMarkers -> {
                loadMarkers()
            }
            is MapUIEvent.ShowError -> {
                mapUIState.value = mapUIState.value.copy(
                    mapError = event.error
                )
            }
        }
    }

    private fun loadMarkers() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").get()
            .addOnSuccessListener { result ->
                val markers = mutableListOf<LatLng>()
                for (document in result) {
                    val lat = document.getDouble("latitude")
                    val lon = document.getDouble("longitude")

                    if (lat != null && lon != null) {
                        markers.add(LatLng(lat, lon))
                        Log.d("MapViewModel", "Marker loaded: Lat: $lat, Lon: $lon")
                    }
                }

                mapUIState.value = mapUIState.value.copy(
                    mapMarkers = markers
                )
                Log.d("MapViewModel", "Total markers loaded: ${markers.size}")
            }
            .addOnFailureListener { exception ->
                mapUIState.value = mapUIState.value.copy(
                    mapError = "Error loading markers: ${exception.message}"
                )
            }
    }


    }