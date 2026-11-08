package com.projach.comics_tracker.ui.screens.login

import com.projach.comics_tracker.domain.validation.PasswordValidationError
import com.projach.comics_tracker.domain.validation.UsernameValidationError
import com.projach.comics_tracker.domain.validation.validatePassword
import com.projach.comics_tracker.domain.validation.validateUsername

data class LoginUserScreenUiModel(
    val username: String = "",
    val usernameError: UsernameValidationError? = null,
    val password: String = "",
    val passwordError: PasswordValidationError? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val formIsValid: Boolean
        get() = validateUsername(username) == null &&
                validatePassword(password) == null
}
