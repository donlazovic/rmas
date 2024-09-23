
package com.example.rmasprojekat18723.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth

class ObjectViewModel : ViewModel() {

    var objectUIState = mutableStateOf(ObjectUIState())

    fun onEvent(event: ObjectUIEvent) {
        when (event) {
            is ObjectUIEvent.TitleChange -> {
                objectUIState.value = objectUIState.value.copy(title = event.title)
            }
            is ObjectUIEvent.DescriptionChange -> {
                objectUIState.value = objectUIState.value.copy(description = event.description)
            }
            is ObjectUIEvent.DurationChange -> {
                objectUIState.value = objectUIState.value.copy(duration = event.duration)
            }
            is ObjectUIEvent.StartTimeChange -> {
                objectUIState.value = objectUIState.value.copy(startTime = event.startTime)
            }
            is ObjectUIEvent.PhotoSelected -> {
                objectUIState.value = objectUIState.value.copy(photoUri = event.photoUri)
            }
            is ObjectUIEvent.AddObjectClicked -> {
                addObject(event.onSuccess, event.currentLocation, event.timestamp)
            }
            is ObjectUIEvent.LoadAllObjects -> {
                loadAllObjects()
            }
            is ObjectUIEvent.RateObject -> {
                rateObject(event.objectId, event.rating, event.onSuccess)
            }
        }
    }

    private fun addObject(onSuccess: () -> Unit, currentLocation: LatLng?, timestamp: Long) {
        val pass = validateDataWithRules()

        if (pass && currentLocation != null) {
            val firestore = FirebaseFirestore.getInstance()
            val storage = FirebaseStorage.getInstance()
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            val userId = currentUser?.uid ?: return
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val username = document.getString("username") ?: "Unknown User"

                    val objectData = hashMapOf(
                        "title" to objectUIState.value.title,
                        "description" to objectUIState.value.description,
                        "duration" to objectUIState.value.duration,
                        "startTime" to timestamp,
                        "latitude" to currentLocation.latitude,
                        "longitude" to currentLocation.longitude,
                        "timestamp" to System.currentTimeMillis(),
                        "avgGrade" to 0f,
                        "postedByUserId" to userId,
                        "postedByUsername" to username
                    )



                    firestore.collection("objects").add(objectData)
                        .addOnSuccessListener { documentRef ->
                            val objectId = documentRef.id
                            objectUIState.value = objectUIState.value.copy(objectId = objectId)

                            objectUIState.value.photoUri?.let { uri ->
                                val storageRef =
                                    storage.reference.child("object_photos/$objectId.jpg")
                                storageRef.putFile(uri)
                                    .addOnSuccessListener {
                                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                            firestore.collection("objects").document(objectId)
                                                .update("photoUrl", downloadUrl.toString())
                                                .addOnSuccessListener {
                                                    updateUserPoints(userId, 1)
                                                    onSuccess()
                                                }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e(
                                            "ObjectViewModel",
                                            "Photo upload failed: ${exception.message}"
                                        )
                                    }
                            } ?: run {
                                updateUserPoints(userId, 1)
                                onSuccess()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("ObjectViewModel", "Failed to add object: ${exception.message}")
                        }
                }
        }
    }

    private fun updateUserPoints(userId: String, pointsToAdd: Int) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef = firestore.collection("users").document(userId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            val currentPoints = documentSnapshot.getLong("points")?.toInt() ?: 0
            val newPoints = currentPoints + pointsToAdd

            userRef.update("points", newPoints)
                .addOnSuccessListener {
                    Log.d("ObjectViewModel", "User points updated: $newPoints")
                }
                .addOnFailureListener { exception ->
                    Log.e("ObjectViewModel", "Failed to update user points: ${exception.message}")
                }
        }
    }

    private fun rateObject(objectId: String, rating: Int, onSuccess: () -> Unit) {
        if (objectId.isEmpty()) {
            Log.e("ObjectViewModel", "objectId is empty")
            return
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("ratings")
            .whereEqualTo("userId", userId)
            .whereEqualTo("objectId", objectId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val newRating = Rating(userId = userId, objectId = objectId, grade = rating)
                    firestore.collection("ratings").add(newRating)
                        .addOnSuccessListener {
                            updateAvgGrade(objectId) {
                                assignPointsForRating(objectId, rating)
                                onSuccess()
                            }
                        }
                } else {
                    val ratingDocument = documents.firstOrNull()
                    val previousRating = ratingDocument?.getLong("grade")?.toInt() ?: 0

                    ratingDocument?.reference?.update("grade", rating)
                        ?.addOnSuccessListener {
                            updateAvgGrade(objectId) {
                                adjustPointsForUpdatedRating(objectId, previousRating, rating)
                                onSuccess()
                            }
                        }
                }
            }
    }

    private fun adjustPointsForUpdatedRating(objectId: String, previousRating: Int, newRating: Int) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").document(objectId).get()
            .addOnSuccessListener { documentSnapshot ->
                val ownerId = documentSnapshot.getString("postedByUserId")
                if (ownerId != null) {
                    val previousPoints = calculatePointsFromRating(previousRating)
                    val newPoints = calculatePointsFromRating(newRating)
                    val pointsDifference = newPoints - previousPoints

                    updateUserPoints(ownerId, pointsDifference)
                }
            }
    }

    private fun calculatePointsFromRating(rating: Int): Int {
        return when (rating) {
            0 -> -4
            1 -> 3
            2 -> 2
            3 -> -1
            4 -> 0
            5 -> 1
            6 -> 2
            7 -> 3
            8 -> 4
            9 -> 5
            10 -> 6
            else -> 0
        }
    }


    private fun assignPointsForRating(objectId: String, rating: Int) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").document(objectId).get()
            .addOnSuccessListener { documentSnapshot ->
                val ownerId = documentSnapshot.getString("postedByUserId")
                if (ownerId != null) {
                    val pointsToAdd = when (rating) {
                        0 -> -4
                        1 -> 3
                        2 -> 2
                        3 -> -1
                        4 -> 0
                        5 -> 1
                        6 -> 2
                        7 -> 3
                        8 -> 4
                        9 -> 5
                        10 -> 6
                        else -> 0
                    }
                    updateUserPoints(ownerId, pointsToAdd)
                }
            }
    }

    private fun updateAvgGrade(objectId: String, onSuccess: () -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("ratings")
            .whereEqualTo("objectId", objectId)
            .get()
            .addOnSuccessListener { result ->
                val totalRatings = result.size()
                val sumRatings = result.documents.sumOf { it.getLong("grade")?.toInt() ?: 0 }

                if (totalRatings > 0) {
                    val avgGrade = sumRatings.toFloat() / totalRatings
                    firestore.collection("objects").document(objectId)
                        .update("avgGrade", avgGrade)
                        .addOnSuccessListener {
                            Log.d("ObjectViewModel", "Average grade is: $avgGrade")
                            updateObjectInMapState(objectId, avgGrade)
                            onSuccess()
                            loadAllObjects()
                        }
                }
            }
    }


    private fun loadAllObjects() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("objects").get()
            .addOnSuccessListener { result ->
                val objects = result.documents.map { document ->
                    ObjectUIState(
                        objectId = document.id,
                        title = document.getString("title") ?: "",
                        description = document.getString("description") ?: "",
                        duration = document.getDouble("duration")?.toFloat() ?: 0f,
                        startTime = document.getLong("startTime") ?: 0L,
                        photoUri = Uri.parse(document.getString("photoUrl") ?: ""),
                        avgGrade = document.getDouble("avgGrade")?.toFloat() ?: 0f,
                        userRatings = mutableMapOf(),
                        latitude = document.getDouble("latitude") ?: 0.0,
                        longitude = document.getDouble("longitude") ?: 0.0
                    )
                }
                objectUIState.value = objectUIState.value.copy(objects = objects)
            }
            .addOnFailureListener { exception ->
                Log.e("ObjectViewModel", "Error loading objects: ${exception.message}")
            }
    }

    private fun updateObjectInMapState(objectId: String, avgGrade: Float) {
        val updatedObjects = objectUIState.value.objects.map { obj ->
            if (obj.objectId == objectId) {
                obj.copy(avgGrade = avgGrade)
            } else {
                obj
            }
        }
        objectUIState.value = objectUIState.value.copy(objects = updatedObjects)
    }

    private fun validateDataWithRules(): Boolean {
        val title = if (objectUIState.value.title.isEmpty()) "Title is required" else null
        val description = if (objectUIState.value.description.isEmpty()) "Description is required" else null
        val duration = if (objectUIState.value.duration <= 0f) "Duration must be greater than 0" else null

        objectUIState.value = objectUIState.value.copy(
            titleError = title,
            descriptionError = description,
            durationError = duration
        )

        return title == null && description == null && duration == null
    }
}
