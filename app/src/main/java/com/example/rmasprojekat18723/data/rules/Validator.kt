package com.example.rmasprojekat18723.data.rules

object Validator {

    fun validateUsername(username: String): String? {
        return when {
            !username.matches(Regex("^[a-zA-Z0-9]+$")) -> "Username can only contain letters and numbers."
            else -> null
        }
    }

    fun validateNameAndSurname(name: String): String? {
        return when {
            !name.matches(Regex("^[a-zA-Z\\s]+$")) -> "Full name can only contain letters and spaces."
            else -> null
        }
    }


    fun validatePhoneNumber(phoneNumber: String): String? {
        return when {
            !phoneNumber.matches(Regex("^[0-9]+$")) -> "Phone number can only contain numbers."
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) -> "Invalid email format."
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.length < 9 -> "Password must be at least 9 characters long."
            password.contains(" ") -> "Password cannot contain spaces."
            !password.matches(Regex(".*[A-Z].*")) -> "Password must contain at least one uppercase letter."
            !password.matches(Regex(".*[a-z].*")) -> "Password must contain at least one lowercase letter."
            !password.matches(Regex(".*[@$!%*?&#].*")) -> "Password must contain at least one special character."
            !password.matches(Regex(".*\\d.*")) -> "Password must contain at least one digit."
            else -> null
        }
    }
}

