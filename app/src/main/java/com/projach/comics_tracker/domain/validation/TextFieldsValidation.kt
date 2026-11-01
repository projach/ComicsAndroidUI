package com.projach.comics_tracker.domain.validation

import com.projach.comics_tracker.domain.PASSWORD_MIN_LENGTH
import com.projach.comics_tracker.domain.USERNAME_MAX_LENGTH
import com.projach.comics_tracker.domain.USERNAME_MIN_LENGTH

enum class ValidationError {
    EMPTY,
    CONTAINS_WHITESPACE,
    INVALID_EMAIL,
    TOO_SHORT,
    TOO_LONG
}

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")
private fun String.hasWhitespace() = any { it.isWhitespace() }

fun validateEmail(input: String): ValidationError? = when {
    input.isEmpty() -> ValidationError.EMPTY
    input.hasWhitespace() -> ValidationError.CONTAINS_WHITESPACE
    !EMAIL_REGEX.matches(input) -> ValidationError.INVALID_EMAIL
    else -> null
}

fun validateUsername(input: String): ValidationError? = when {
    input.isEmpty() -> ValidationError.EMPTY
    input.hasWhitespace() -> ValidationError.CONTAINS_WHITESPACE
    input.length < USERNAME_MIN_LENGTH -> ValidationError.TOO_SHORT
    input.length > USERNAME_MAX_LENGTH -> ValidationError.TOO_LONG
    else -> null
}

fun validatePassword(input: String): ValidationError? = when {
    input.isEmpty() -> ValidationError.EMPTY
    input.hasWhitespace() -> ValidationError.CONTAINS_WHITESPACE
    input.length < PASSWORD_MIN_LENGTH -> ValidationError.TOO_SHORT
    else -> null
}