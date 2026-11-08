package com.projach.comics_tracker.domain.validation

sealed interface CommonValidationError {
    object Empty : CommonValidationError
    object ContainsWhitespaces : CommonValidationError
}

sealed interface EmailValidationError {
    data class Common(val error: CommonValidationError) : EmailValidationError
    object InvalidEmail : EmailValidationError
    object Conflict : EmailValidationError
}

sealed interface UsernameValidationError {
    data class Common(val error: CommonValidationError) : UsernameValidationError
    object TooShort : UsernameValidationError
    object TooLong : UsernameValidationError
    object Conflict : UsernameValidationError
}

sealed interface PasswordValidationError {
    data class Common(val error: CommonValidationError) : PasswordValidationError
    object TooShort : PasswordValidationError
}