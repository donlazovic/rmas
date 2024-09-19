package com.example.rmasprojekat18723.data

import com.google.android.gms.maps.model.LatLng

sealed class MapUIEvent {
    data class LocationUpdate(val location: LatLng) : MapUIEvent()
    object LoadMarkers : MapUIEvent()
    data class ShowError(val error: String) : MapUIEvent()
    data class ApplyFilters(val filters: Filters) : MapUIEvent()

}