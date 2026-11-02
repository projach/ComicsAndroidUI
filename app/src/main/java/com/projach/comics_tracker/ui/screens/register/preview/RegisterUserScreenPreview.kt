package com.projach.comics_tracker.ui.screens.register.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.ui.screens.register.Content

@Preview
@Composable
fun RegisterUserScreenPreview(){
    Content(
        emailText = "",
        onEmailChanged = { },
        emailError = null,
        usernameText = "",
        onUsernameChanged = { },
        usernameError = null,
        passwordText = "",
        onPasswordChanged = { },
        passwordError = null,
        canSubmit = false,
        isLoading = false,
        onSubmit = {  }
    )
}