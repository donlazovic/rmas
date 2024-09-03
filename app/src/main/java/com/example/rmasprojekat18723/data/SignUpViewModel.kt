package com.example.rmasprojekat18723.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rmasprojekat18723.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

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


        }
    }

    private fun signUp(onSuccess: () -> Unit) {

        val pass = validateDataWithRules()

        if (pass) {
            createUserInFirebase(
                email = registrationUIState.value.email,
                password = registrationUIState.value.password,
                onSuccess = onSuccess,
            )
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

    private fun createUserInFirebase(email: String , password: String,onSuccess: () -> Unit) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onSuccess()
                }
            }
            .addOnFailureListener{

            }

    }

}