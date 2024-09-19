package com.example.rmasprojekat18723.data

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

sealed class ObjectUIEvent {
    data class TitleChange(val title: String) : ObjectUIEvent()
    data class DescriptionChange(val description: String) : ObjectUIEvent()
    data class DurationChange(val duration: Float) : ObjectUIEvent()
    data class StartTimeChange(val startTime: Long) : ObjectUIEvent()
    data class PhotoSelected(val photoUri: Uri?) : ObjectUIEvent()

    data class AddObjectClicked(val onSuccess: () -> Unit, val currentLocation: LatLng?,val timestamp: Long) : ObjectUIEvent()
    data class RateObject(val objectId: String, val rating: Int, val onSuccess: () -> Unit) : ObjectUIEvent()
    object LoadAllObjects : ObjectUIEvent()

}