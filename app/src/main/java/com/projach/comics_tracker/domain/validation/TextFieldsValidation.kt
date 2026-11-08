package com.projach.comics_tracker.domain.validation

const val USERNAME_MIN_LENGTH = 4
const val USERNAME_MAX_LENGTH = 14
const val PASSWORD_MIN_LENGTH = 8

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")
private fun String.hasWhitespace() = any { it.isWhitespace() }

fun validateEmail(input: String): EmailValidationError? = when {
    input.isEmpty() -> EmailValidationError.Common(CommonValidationError.Empty)
    input.hasWhitespace() -> EmailValidationError.Common(CommonValidationError.ContainsWhitespaces)
    !EMAIL_REGEX.matches(input) -> EmailValidationError.InvalidEmail
    else -> null
}

fun validateUsername(input: String): UsernameValidationError? = when {
    input.isEmpty() -> UsernameValidationError.Common(CommonValidationError.Empty)
    input.hasWhitespace() -> UsernameValidationError.Common(CommonValidationError.ContainsWhitespaces)
    input.length < USERNAME_MIN_LENGTH -> UsernameValidationError.TooShort
    input.length > USERNAME_MAX_LENGTH -> UsernameValidationError.TooLong
    else -> null
}

fun validatePassword(input: String): PasswordValidationError? = when {
    input.isEmpty() -> PasswordValidationError.Common(CommonValidationError.Empty)
    input.hasWhitespace() -> PasswordValidationError.Common(CommonValidationError.ContainsWhitespaces)
    input.length < PASSWORD_MIN_LENGTH -> PasswordValidationError.TooShort
    else -> null
}