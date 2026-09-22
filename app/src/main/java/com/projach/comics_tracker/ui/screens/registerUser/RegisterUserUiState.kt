package com.projach.comics_tracker.ui.screens.registerUser

import com.projach.comics_tracker.domain.models.UiModel

data class RegisterUserUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null
) : UiModel
