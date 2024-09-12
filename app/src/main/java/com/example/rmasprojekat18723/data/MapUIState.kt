package com.example.rmasprojekat18723.data


import com.google.android.gms.maps.model.LatLng

data class MapUIState(
    var currentLocation: LatLng? = null,
    var mapMarkers: List<LatLng> = emptyList(),
    var mapError: String? = null
)