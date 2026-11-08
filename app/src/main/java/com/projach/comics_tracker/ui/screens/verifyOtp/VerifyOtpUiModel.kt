package com.projach.comics_tracker.ui.screens.verifyOtp

data class VerifyOtpUiModel(
    val code: String = "",
    val error: String? = null,
    val showExitDialog: Boolean = false,
    val isLoading: Boolean = false,
    val canRefreshCode: Boolean = false,
    val showTimer: Boolean = false,
    val isSuccess: Boolean = false
)
