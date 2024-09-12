package com.example.rmasprojekat18723.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

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
        val markers = listOf(
            LatLng(44.7866, 20.4489),
            LatLng(44.8206, 20.4622)
        )
        mapUIState.value = mapUIState.value.copy(
            mapMarkers = markers
        )
    }
}