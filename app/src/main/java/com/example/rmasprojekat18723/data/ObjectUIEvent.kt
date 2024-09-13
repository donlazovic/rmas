package com.example.rmasprojekat18723.data

import android.net.Uri

sealed class ObjectUIEvent {
    data class TitleChange(val title: String) : ObjectUIEvent()
    data class DescriptionChange(val description: String) : ObjectUIEvent()
    data class DurationChange(val duration: String) : ObjectUIEvent()
    data class StartTimeChange(val startTime: String) : ObjectUIEvent()
    data class PhotoSelected(val photoUri: Uri?) : ObjectUIEvent()

    data class AddObjectClicked(val onSuccess: () -> Unit) : ObjectUIEvent()
}