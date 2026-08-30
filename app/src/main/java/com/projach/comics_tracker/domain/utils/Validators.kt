package com.projach.comics_tracker.domain.utils

object Validators {
    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun isValidEmail(email: String): Boolean = emailRegex.matches(email)
}