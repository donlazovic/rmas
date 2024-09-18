package com.example.rmasprojekat18723.data

import android.net.Uri


data class RegistrationUIState (
    var userId: String = "",
    var username : String = "",
    var name : String = "",
    var surname : String = "",
    var phoneNumber : String = "",
    var email : String = "",
    var password : String = "",
    var confirmPassword : String = "",
    var profileImageUri: Uri? = null,
    var points: Int = 0,


    var usernameError : String? = null,
    var nameError : String? = null,
    var surnameError : String? = null,
    var phoneNumberError : String? = null,
    var emailError : String? = null,
    var passwordError : String? = null,
    var confirmPasswordError : String? = null,

    )