package com.projach.comics_tracker.ui.screens.signUp

data class RegisterUserUiModel(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
)
