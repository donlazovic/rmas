package com.example.rmasprojekat18723.data


data class LogInUIState(
        var email : String = "",
        var password : String = "",

        var emailError : String? = null,
        var passwordError : String? = null,
        var loginError: String? = null,
)