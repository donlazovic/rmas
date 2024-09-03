package com.example.rmasprojekat18723.data

sealed class LogInUIEvent {

    data class EmailChange(val email : String) : LogInUIEvent()
    data class PasswordChange(val password : String) : LogInUIEvent()

    data class LoginClicked(val onSuccess: () -> Unit) : LogInUIEvent()
}