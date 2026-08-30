package com.projach.comics_tracker.ui.screens.registerUser

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun RegisterUserScreenPreview() {
    Content(
        onRegisterClick = { _, _, _ -> },
        isFormValid = true,
        emailState = TextFieldState(),
        usernameState = TextFieldState(),
        passwordState = TextFieldState(),
        emailError = null,
        usernameError = null,
        passwordError = null,
        onLoginClick = {}
    )
}