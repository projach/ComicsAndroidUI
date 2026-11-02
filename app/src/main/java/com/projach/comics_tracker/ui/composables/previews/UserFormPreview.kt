package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.UserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel
import com.projach.comics_tracker.ui.composables.models.FieldType

@Preview
@Composable
fun UserFormPreview(){
    UserForm(
        listOf(
            ComicsTestFieldUiModel(
                text = "Email",
                label = stringResource(R.string.register_user_email),
                hint = stringResource(R.string.register_user_email_hint),
                onValueChanged = {},
                fieldType = FieldType.Email
            ),
            ComicsTestFieldUiModel(
                text = "Username",
                label = stringResource(R.string.register_user_username),
                hint = stringResource(R.string.register_user_username_hint),
                onValueChanged = {},
                fieldType = FieldType.Text
            ),
            ComicsTestFieldUiModel(
                text = "",
                label = stringResource(R.string.register_user_password),
                hint = stringResource(R.string.register_user_password_hint),
                onValueChanged = {},
                fieldType = FieldType.Password
            )
        ),
        onSubmit = {}
    )
}