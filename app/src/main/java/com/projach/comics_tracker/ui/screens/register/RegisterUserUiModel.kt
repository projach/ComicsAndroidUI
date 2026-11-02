package com.projach.comics_tracker.ui.screens.register

import com.projach.comics_tracker.domain.validation.EmailValidationError
import com.projach.comics_tracker.domain.validation.PasswordValidationError
import com.projach.comics_tracker.domain.validation.UsernameValidationError
import com.projach.comics_tracker.domain.validation.validateEmail
import com.projach.comics_tracker.domain.validation.validatePassword
import com.projach.comics_tracker.domain.validation.validateUsername

data class RegisterUserUiModel(
    val email: String = "",
    val emailError: EmailValidationError? = null,
    val password: String = "",
    val passwordError: PasswordValidationError? = null,
    val username: String = "",
    val usernameError: UsernameValidationError? = null,
    val isLoading: Boolean = false,
) {
    val formIsValid: Boolean
        get() = validateUsername(username) == null &&
                validateEmail(email) == null &&
                validatePassword(password) == null
}
