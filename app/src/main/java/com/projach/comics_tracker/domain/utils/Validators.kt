package com.projach.comics_tracker.domain.utils

object Validators {
    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun isValidEmail(email: String): Boolean = emailRegex.matches(email)

    fun validateEmail(email: String): String? = when {
        email.isEmpty() -> null
        !isValidEmail(email) -> "Enter a valid email"
        else -> null
    }

    fun validateUsername(username: String): String? = when {
        username.isEmpty() -> null
        username.length !in 4..14 -> "Username needs to be 4 to 14 characters"
        else -> null
    }

    fun validatePassword(password: String): String? = when {
        password.isEmpty() -> null
        password.length < 8 -> "Password needs more than 8 characters"
        else -> null
    }
}