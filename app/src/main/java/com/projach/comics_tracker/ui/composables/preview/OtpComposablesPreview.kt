package com.projach.comics_tracker.ui.composables.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.ui.composables.OtpInputField

@Preview
@Composable
fun OtpComposablesPreview() {
    OtpInputField(
        onCodeComplete = {},
        value = "",
        onValueChange = {},
        error = "The code is not correct"
    )
}