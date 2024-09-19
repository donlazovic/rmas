package com.example.rmasprojekat18723.data

import android.location.Location
import android.net.Uri
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
            is MapUIEvent.ApplyFilters -> {
                applyFilters(event.filters)
            }
        }
    }

    private fun applyFilters(filters: Filters) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").get()
            .addOnSuccessListener { result ->
                val filteredObjects = result.documents.mapNotNull { document ->
                    val author = document.getString("postedByUsername") ?: ""
                    val avgGrade = document.getDouble("avgGrade")?.toInt() ?: 0
                    val startTime = document.getLong("startTime") ?: 0L
                    val duration = document.getLong("duration")?.toFloat() ?: 0f
                    val objectLat = document.getDouble("latitude") ?: 0.0
                    val objectLng = document.getDouble("longitude") ?: 0.0
                    val objectLocation = LatLng(objectLat, objectLng)

                    val isWithinRadius = filters.radius?.let { radius ->
                        val userLocation = mapUIState.value.currentLocation
                        if (userLocation != null) {
                            val distance = FloatArray(1)
                            Location.distanceBetween(
                                userLocation.latitude,
                                userLocation.longitude,
                                objectLocation.latitude,
                                objectLocation.longitude,
                                distance
                            )
                            distance[0] <= radius
                        } else true
                    } ?: true

                    if (isWithinRadius &&
                        (filters.author.isEmpty() || author.startsWith(filters.author, true)) &&
                        (filters.ratingFrom == null || avgGrade >= filters.ratingFrom) &&
                        (filters.ratingTo == null || avgGrade <= filters.ratingTo) &&
                        (filters.startDate == null || startTime >= filters.startDate) &&
                        (filters.endDate == null || startTime <= filters.endDate) &&
                        (filters.durationFrom == null || duration >= filters.durationFrom) &&
                        (filters.durationTo == null || duration <= filters.durationTo)
                    ) {
                        Log.d("FilterCheck", "Object passed filter: ${document.id}, author: $author, avgGrade: $avgGrade")
                        ObjectUIState(
                            objectId = document.id,
                            title = document.getString("title") ?: "",
                            description = document.getString("description") ?: "",
                            duration = duration,
                            startTime = startTime,
                            photoUri = Uri.parse(document.getString("photoUrl") ?: ""),
                            avgGrade = document.getDouble("avgGrade")?.toFloat() ?: 0f,
                            latitude = document.getDouble("latitude") ?: 0.0,
                            longitude = document.getDouble("longitude") ?: 0.0,
                            postedByUsername = author,
                            postedByUserId = document.getString("postedByUserId") ?: ""
                        )
                    } else null
                }

                mapUIState.value = mapUIState.value.copy(objects = filteredObjects)

                val filteredMarkers = filteredObjects.map { obj -> LatLng(obj.latitude, obj.longitude) }
                mapUIState.value = mapUIState.value.copy(mapMarkers = filteredMarkers)

                loadFilteredMarkers(filteredMarkers)
            }
    }

    private fun loadFilteredMarkers(filteredMarkers: List<LatLng>) {
        val mapMarkers = filteredMarkers.map { markerLocation ->
            LatLng(markerLocation.latitude, markerLocation.longitude)
        }
        mapUIState.value = mapUIState.value.copy(mapMarkers = mapMarkers)
    }

    private fun loadMarkers() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").get()
            .addOnSuccessListener { result ->
                val markers = mutableListOf<LatLng>()
                val objects = mutableListOf<ObjectUIState>()
                for (document in result) {
                    val lat = document.getDouble("latitude")
                    val lon = document.getDouble("longitude")

                    if (lat != null && lon != null) {
                        markers.add(LatLng(lat, lon))

                        val obj = ObjectUIState(
                            objectId = document.id,
                            title = document.getString("title") ?: "",
                            description = document.getString("description") ?: "",
                            duration = document.getDouble("duration")?.toFloat() ?: 0f,
                            startTime = document.getLong("startTime") ?: 0L,
                            photoUri = Uri.parse(document.getString("photoUrl") ?: ""),
                            avgGrade = document.getDouble("avgGrade")?.toFloat() ?: 0f,
                            latitude = lat,
                            longitude = lon,
                            postedByUsername = document.getString("postedByUsername") ?: "Unknown User",
                            postedByUserId = document.getString("postedByUserId") ?: "")
                        objects.add(obj)
                    }
                }

                mapUIState.value = mapUIState.value.copy(
                    mapMarkers = markers,
                    objects = objects
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