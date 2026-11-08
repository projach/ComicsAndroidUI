package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.ui.composables.OtpInput

@Preview
@Composable
fun OtpInputPreview() {
    OtpInput(
        otpCode = "32",
        onOtpCodeChange = {},
        otpLength = 6,
        onOtpComplete = {},
        error = null
    )
}