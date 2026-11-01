package com.projach.comics_tracker.ui.screens.register

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
import com.projach.comics_tracker.domain.validation.ValidationError
import com.projach.comics_tracker.ui.composables.UserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel
import com.projach.comics_tracker.ui.composables.models.FieldType
import com.projach.comics_tracker.ui.theme.Typography

@Composable
fun RegisterUserScreen(viewModel: RegisterUserViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        emailText = uiState.value.email,
        onEmailChanged = { viewModel.onIntent(RegisterUserIntent.EmailChanged(it)) },
        emailError = uiState.value.emailError,
        usernameText = uiState.value.username,
        onUsernameChanged = { viewModel.onIntent(RegisterUserIntent.UsernameChanged(it)) },
        usernameError = uiState.value.usernameError,
        passwordText = uiState.value.password,
        onPasswordChanged = { viewModel.onIntent(RegisterUserIntent.PasswordChanged(it)) },
        passwordError = uiState.value.passwordError,
        canSubmit = uiState.value.formIsValid,
        isLoading = uiState.value.isLoading,
        onSubmit = { viewModel.onIntent(intent = RegisterUserIntent.Submit) }
    )
}

@Composable
fun Content(
    emailText: String,
    onEmailChanged: (String) -> Unit,
    emailError: ValidationError?,
    usernameText: String,
    onUsernameChanged: (String) -> Unit,
    usernameError: ValidationError?,
    passwordText: String,
    onPasswordChanged: (String) -> Unit,
    passwordError: ValidationError?,
    canSubmit: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
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
    Column(modifier = Modifier
        .padding(horizontal = 12.dp, vertical = 20.dp)
        .imePadding()) {
        Text(
            text = stringResource(R.string.register_user_create_account_label),
            style = Typography.titleLarge
        )
        Spacer(Modifier.height(24.dp))
        UserForm(
            listOf(
                ComicsTestFieldUiModel(
                    text = usernameText,
                    label = stringResource(R.string.register_user_username),
                    hint = stringResource(R.string.register_user_username_hint),
                    error = when (usernameError) {
                        ValidationError.CONTAINS_WHITESPACE -> stringResource(R.string.register_user_username_contains_whitespace)
                        ValidationError.TOO_LONG -> stringResource(R.string.register_user_username_is_too_long)
                        ValidationError.TOO_SHORT -> if (usernameText.isNotEmpty()) stringResource(R.string.register_user_username_is_too_short) else null
                        ValidationError.EMPTY -> null
                        ValidationError.INVALID_EMAIL -> null
                        null -> null
                    },
                    onValueChanged = onUsernameChanged,
                    fieldType = FieldType.Text
                ),
                ComicsTestFieldUiModel(
                    text = emailText,
                    label = stringResource(R.string.register_user_email),
                    hint = stringResource(R.string.register_user_email_hint),
                    error = when (emailError) {
                        ValidationError.CONTAINS_WHITESPACE -> stringResource(R.string.register_user_email_error)
                        ValidationError.TOO_LONG -> null
                        ValidationError.TOO_SHORT -> null
                        ValidationError.EMPTY -> null
                        ValidationError.INVALID_EMAIL -> stringResource(R.string.register_user_email_error)
                        null -> null
                    },
                    onValueChanged = onEmailChanged,
                    fieldType = FieldType.Email
                ),
                ComicsTestFieldUiModel(
                    text = passwordText,
                    label = stringResource(R.string.register_user_password),
                    hint = stringResource(R.string.register_user_password_hint),
                    error = when (passwordError) {
                        ValidationError.CONTAINS_WHITESPACE -> stringResource(R.string.register_user_password_contains_whitespace)
                        ValidationError.TOO_LONG -> null
                        ValidationError.TOO_SHORT -> if (usernameText.isNotEmpty()) stringResource(R.string.register_user_password_is_too_short) else null
                        ValidationError.EMPTY -> null
                        ValidationError.INVALID_EMAIL -> null
                        null -> null
                    },
                    onValueChanged = onPasswordChanged,
                    fieldType = FieldType.Password
                )
            ),
            onSubmit = onSubmit
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onSubmit,
            enabled = canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.register_user_submit))
        }
    }
}