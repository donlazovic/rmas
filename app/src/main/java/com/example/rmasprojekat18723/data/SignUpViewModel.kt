package com.example.rmasprojekat18723.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rmasprojekat18723.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SignUpViewModel : ViewModel() {

    var registrationUIState = mutableStateOf(RegistrationUIState())


    fun onEvent(event: SignUpUIEvent) {
        when (event) {
            is SignUpUIEvent.UsernameChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    username = event.username
                )
            }

            is SignUpUIEvent.NameChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    name = event.name
                )
            }

            is SignUpUIEvent.SurnameChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    surname = event.surname
                )
            }

            is SignUpUIEvent.PhoneNumberChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    phoneNumber = event.phoneNumber
                )
            }

            is SignUpUIEvent.EmailChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }

            is SignUpUIEvent.PasswordChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }

            is SignUpUIEvent.ConfirmPasswordChange -> {
                registrationUIState.value = registrationUIState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }

            is SignUpUIEvent.RegisterClicked -> {
                signUp(event.onSuccess)
            }

            is SignUpUIEvent.SignOutClicked -> {
                signOut(event.onSignOut)
            }
            is SignUpUIEvent.ImageSelected -> {
                registrationUIState.value = registrationUIState.value.copy(
                    profileImageUri = event.imageUri
                )
            }


        }
    }

    private fun signUp(onSuccess: () -> Unit) {

        val pass = validateDataWithRules()

        if (pass) {
            registerUser(
                username = registrationUIState.value.username,
                email = registrationUIState.value.email,
                password = registrationUIState.value.password,
                name = registrationUIState.value.name,
                surname = registrationUIState.value.surname,
                phoneNumber = registrationUIState.value.phoneNumber,
                imageUri = registrationUIState.value.profileImageUri,
                onResult = { success, message ->
                    if (success) {
                        onSuccess()
                    } else {
                        Log.e("SignUpViewModel", "Registration failed: $message")
                    }
                }
            )
        } else {
            Log.e("SignUpViewModel", "Validation failed")
        }
    }


    private fun signOut(signOut: () -> Unit) {

        val auth = FirebaseAuth.getInstance()

        auth.signOut()

        val authStateListener = AuthStateListener {
            if (it.currentUser == null) {
                signOut()
            }
        }

        auth.addAuthStateListener(authStateListener)
    }


    private fun validateDataWithRules() : Boolean {
        val username = Validator.validateUsername( username = registrationUIState.value.username)
        val name = Validator.validateNameAndSurname( name = registrationUIState.value.name)
        val surname = Validator.validateNameAndSurname( name = registrationUIState.value.surname)
        val phoneNumber = Validator.validatePhoneNumber( phoneNumber = registrationUIState.value.phoneNumber)
        val email = Validator.validateEmail( email = registrationUIState.value.email)
        val password = Validator.validatePassword( password = registrationUIState.value.password)
        val confirmPassword = Validator.validatePassword( password = registrationUIState.value.confirmPassword)

        registrationUIState.value = registrationUIState.value.copy(
            usernameError = username,
            nameError = name,
            surnameError = surname,
            phoneNumberError = phoneNumber,
            emailError = email,
            passwordError = password,
            confirmPasswordError = confirmPassword,
        )

        return !(username != null || name != null || surname != null || phoneNumber != null || email != null
                || password != null || confirmPassword != null)

    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        name: String,
        surname : String,
        phoneNumber: String,
        imageUri: Uri?,
        onResult: (Boolean, String?) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val userId = user.uid
                        val profileData = hashMapOf(
                            "username" to username,
                            "name" to name,
                            "surname" to surname,
                            "phoneNumber" to phoneNumber,
                            "email" to email,
                            "points" to 0
                        )

                        // Čuvanje podataka u Firestore
                        FirebaseFirestore.getInstance().collection("users")
                            .document(userId).set(profileData)
                            .addOnSuccessListener {
                                Log.d("SignUpViewModel", "User data stored in Firestore")
                                // Upload slike ako postoji
                                imageUri?.let { uri ->
                                    val storageRef = FirebaseStorage.getInstance()
                                        .reference.child("profile_photos/$userId.jpg")
                                    storageRef.putFile(uri)
                                        .addOnSuccessListener {
                                            Log.d("SignUpViewModel", "Image uploaded successfully")
                                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                                FirebaseFirestore.getInstance().collection("users")
                                                    .document(userId)
                                                    .update("photoUrl", downloadUrl.toString())
                                                    .addOnSuccessListener {
                                                        Log.d("SignUpViewModel", "Image URL stored in Firestore")
                                                        onResult(true, null)
                                                    }
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("SignUpViewModel", "Image upload failed: ${exception.message}")
                                            onResult(false, exception.message)
                                        }
                                } ?: run {
                                    onResult(true, null)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("SignUpViewModel", "Error storing user data in Firestore: ${exception.message}")
                                onResult(false, exception.message)
                            }
                    } else {
                        onResult(false, "User ID is null")
                    }
                } else {
                    Log.e("SignUpViewModel", "Error creating user: ${task.exception?.message}")
                    onResult(false, task.exception?.message)
                }
            }
    }


}