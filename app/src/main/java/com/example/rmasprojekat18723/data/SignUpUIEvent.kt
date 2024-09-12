package com.example.rmasprojekat18723.data

import android.net.Uri

sealed class SignUpUIEvent {

    data class UsernameChange(val username : String) : SignUpUIEvent()
    data class NameChange(val name : String) : SignUpUIEvent()
    data class SurnameChange(val surname : String) : SignUpUIEvent()
    data class PhoneNumberChange(val phoneNumber : String) : SignUpUIEvent()
    data class EmailChange(val email : String) : SignUpUIEvent()
    data class PasswordChange(val password : String) : SignUpUIEvent()
    data class ConfirmPasswordChange(val confirmPassword : String) : SignUpUIEvent()

    data class RegisterClicked(val onSuccess: () -> Unit) : SignUpUIEvent()

    data class SignOutClicked(val onSignOut: () -> Unit) : SignUpUIEvent()

    data class ImageSelected(val imageUri: Uri?) : SignUpUIEvent()


}

