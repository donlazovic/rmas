package com.example.rmasprojekat18723.data

import android.net.Uri

data class ObjectUIState(
    var objectId: String = "",
    var title: String = "",
    var description: String = "",
    var duration: String = "",
    var startTime: String = "",
    var photoUri: Uri? = null,
    var objects: List<ObjectUIState> = emptyList(),
    var avgGrade: Float = 0f,
    var userRatings: MutableMap<String, Int> = mutableMapOf(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var postedByUsername: String = "",
    var postedByUserId: String = "",

    var titleError: String? = null,
    var descriptionError: String? = null,
    var durationError: String? = null,
    var startTimeError: String? = null,
)
