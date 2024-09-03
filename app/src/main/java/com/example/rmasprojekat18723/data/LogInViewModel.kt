package com.example.rmasprojekat18723.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rmasprojekat18723.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {

    var loginUIState = mutableStateOf(LogInUIState())

    fun onEvent(event: LogInUIEvent) {
        when (event) {
            is LogInUIEvent.EmailChange -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LogInUIEvent.PasswordChange -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LogInUIEvent.LoginClicked -> {
                login(event.onSuccess)
            }
        }
    }

    private fun login(onSuccess: () -> Unit) {

        val pass = validateDataWithRules()

        if (pass){
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(loginUIState.value.email, loginUIState.value.password)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        loginUIState.value = loginUIState.value.copy(
                            loginError = null
                        )
                        onSuccess()
                        }
                    else {
                        loginUIState.value = loginUIState.value.copy(
                            loginError = "Invalid email or password."
                        )
                    }
                }
                .addOnFailureListener{
                    loginUIState.value = loginUIState.value.copy(
                        loginError = "Invalid email or password."
                    )
                }

        }

    }

    private fun validateDataWithRules() : Boolean {
        val email = Validator.validateEmail( email = loginUIState.value.email)
        val password = Validator.validatePassword( password = loginUIState.value.password)

        loginUIState.value = loginUIState.value.copy(
            emailError = email,
            passwordError = password,
        )

        return !(email != null || password != null)

    }

}