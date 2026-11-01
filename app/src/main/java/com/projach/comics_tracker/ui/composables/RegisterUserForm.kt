package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Composable
fun RegisterUserForm(
    emailTextFieldUiModel: ComicsTestFieldUiModel,
    usernameTextFieldUiModel: ComicsTestFieldUiModel,
    passwordTextFieldUiModel: ComicsTestFieldUiModel,
    onSubmit: () -> Unit
){
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ComicsTextField(
            usernameTextFieldUiModel,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}),
        )
        ComicsTextField(
            emailTextFieldUiModel,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}),
        )
        ComicsTextField(
            passwordTextFieldUiModel,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onSubmit.invoke()
            })
        )
    }
}

