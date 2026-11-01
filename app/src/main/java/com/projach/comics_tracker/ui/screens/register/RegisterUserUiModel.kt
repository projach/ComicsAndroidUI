package com.projach.comics_tracker.ui.screens.register

import com.projach.comics_tracker.domain.validation.ValidationError
import com.projach.comics_tracker.domain.validation.validateEmail
import com.projach.comics_tracker.domain.validation.validatePassword
import com.projach.comics_tracker.domain.validation.validateUsername

data class RegisterUserUiModel(
    val email: String = "",
    val emailError: ValidationError? = null,
    val password: String = "",
    val passwordError: ValidationError? = null,
    val username: String = "",
    val usernameError: ValidationError? = null,
    val isLoading: Boolean = false,
){
    val formIsValid: Boolean
        get() = validateUsername(username) == null &&
                validateEmail(email) == null &&
                validatePassword(password) == null
}
