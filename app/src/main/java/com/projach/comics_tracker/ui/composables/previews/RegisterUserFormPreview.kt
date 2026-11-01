package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.RegisterUserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Preview
@Composable
fun RegisterUserFormPreview(){
    RegisterUserForm(
        emailTextFieldUiModel = ComicsTestFieldUiModel(
            label = stringResource(R.string.register_user_username),
            hint = stringResource(R.string.register_user_username_hint),
            onTextChanged = {},
        ),
        usernameTextFieldUiModel = ComicsTestFieldUiModel(
            label = stringResource(R.string.register_user_email),
            hint = stringResource(R.string.register_user_email_hint),
            onTextChanged = {},
        ),
        passwordTextFieldUiModel = ComicsTestFieldUiModel(
            label = stringResource(R.string.register_user_password),
            hint = stringResource(R.string.register_user_password_hint),
            onTextChanged = {},
        ),
        onSubmit = {}
    )
}