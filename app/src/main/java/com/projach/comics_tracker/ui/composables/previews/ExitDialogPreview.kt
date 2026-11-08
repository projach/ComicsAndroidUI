package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.ExitDialog

@Preview(showBackground = true)
@Composable
fun ExitDialogPreview() {
    ExitDialog(
        description = stringResource(R.string.otp_exit_dialog_description),
        exitText = stringResource(R.string.otp_exit_dialog_exit),
        stayText = stringResource(R.string.otp_exit_dialog_stay),
        onExitPress = {},
        onStayPress = {}
    )
}