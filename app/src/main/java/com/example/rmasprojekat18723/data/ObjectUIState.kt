package com.example.rmasprojekat18723.data

import android.net.Uri

data class ObjectUIState(
    var title: String = "",
    var description: String = "",
    var duration: String = "",
    var startTime: String = "",
    var photoUri: Uri? = null,
    var objects: List<ObjectUIState> = emptyList(),

    var titleError: String? = null,
    var descriptionError: String? = null,
    var durationError: String? = null,
    var startTimeError: String? = null,
)
