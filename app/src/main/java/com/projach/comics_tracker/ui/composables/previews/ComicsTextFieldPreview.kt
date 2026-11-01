package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.ComicsTextField
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Preview
@Composable
fun ComicsTextFieldPreview(){
    ComicsTextField(
        comicsTestFieldUiModel = ComicsTestFieldUiModel(
            label = stringResource(R.string.register_user_email),
            hint = stringResource(R.string.register_user_email_hint),
            text = "",
            onTextChanged = {},
        ),
        keyboardOptions = KeyboardOptions(),
        keyboardActions = KeyboardActions()
    )
}