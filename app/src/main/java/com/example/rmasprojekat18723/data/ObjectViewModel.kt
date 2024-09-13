package com.example.rmasprojekat18723.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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
                addObject(event.onSuccess)
            }
        }
    }

    private fun addObject(onSuccess: () -> Unit) {
        val pass = validateDataWithRules()

        if (pass) {
            val firestore = FirebaseFirestore.getInstance()
            val storage = FirebaseStorage.getInstance()

            val objectData = hashMapOf(
                "title" to objectUIState.value.title,
                "description" to objectUIState.value.description,
                "duration" to objectUIState.value.duration,
                "startTime" to objectUIState.value.startTime,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("objects").add(objectData)
                .addOnSuccessListener { documentRef ->
                    val objectId = documentRef.id
                    objectUIState.value.photoUri?.let { uri ->
                        val storageRef = storage.reference.child("object_photos/$objectId.jpg")
                        storageRef.putFile(uri)
                            .addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                    firestore.collection("objects").document(objectId)
                                        .update("photoUrl", downloadUrl.toString())
                                        .addOnSuccessListener {
                                            onSuccess()
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ObjectViewModel", "Photo upload failed: ${exception.message}")
                            }
                    } ?: onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e("ObjectViewModel", "Failed to add object: ${exception.message}")
                }
        }
    }

    private fun validateDataWithRules(): Boolean {
        val title = if (objectUIState.value.title.isEmpty()) "Title is required" else null
        val description = if (objectUIState.value.description.isEmpty()) "Description is required" else null
        val duration = if (objectUIState.value.duration.isEmpty()) "Duration is required" else null

        objectUIState.value = objectUIState.value.copy(
            titleError = title,
            descriptionError = description,
            durationError = duration
        )

        return title == null && description == null && duration == null
    }
}