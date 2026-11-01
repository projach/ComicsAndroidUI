package com.projach.comics_tracker.ui.screens.signUp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.RegisterUserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Composable
fun RegisterUserScreen(viewModel: RegisterUserViewModel){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Screen(
        emailText = uiState.value.email,
        onEmailChanged = { viewModel.onIntent(RegisterUserIntent.EmailChanged(it)) },
        usernameText = uiState.value.username,
        onUsernameChanged = { viewModel.onIntent(RegisterUserIntent.UsernameChanged(it)) },
        passwordText = uiState.value.username,
        onPasswordChanged = { viewModel.onIntent(RegisterUserIntent.PasswordChanged(it)) },
        canSubmit = uiState.value.canSubmit,
        isLoading = uiState.value.isLoading,
        onSubmit = { viewModel.onIntent(intent = RegisterUserIntent.Submit) }
    )
}

@Composable
fun Screen(
    emailText: String,
    onEmailChanged: (String) -> Unit,
    usernameText: String,
    onUsernameChanged: (String) -> Unit,
    passwordText: String,
    onPasswordChanged: (String) -> Unit,
    canSubmit: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit
){
    if (isLoading) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).imePadding()) {
        Text(stringResource(R.string.register_user_create_account_label))
        Spacer(Modifier.height(16.dp))
        RegisterUserForm(
            emailTextFieldUiModel = ComicsTestFieldUiModel(
                text = emailText,
                label = stringResource(R.string.register_user_email),
                hint = stringResource(R.string.register_user_email_hint),
                onTextChanged = onEmailChanged
            ),
            usernameTextFieldUiModel = ComicsTestFieldUiModel(
                text = usernameText,
                label = stringResource(R.string.register_user_username),
                hint = stringResource(R.string.register_user_username_hint),
                onTextChanged = onUsernameChanged
            ),
            passwordTextFieldUiModel = ComicsTestFieldUiModel(
                text = passwordText,
                label = stringResource(R.string.register_user_password),
                hint = stringResource(R.string.register_user_password_hint),
                onTextChanged = onPasswordChanged
            ),
            onSubmit = onSubmit
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onSubmit,
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.register_user_submit))
        }
    }
}