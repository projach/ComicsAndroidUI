package com.projach.comics_tracker.ui.screens.registerUser.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.ui.screens.registerUser.Content

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
        onLoginClick = {},
        isLoading = false
    )
}