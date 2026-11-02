package com.projach.comics_tracker.ui.screens.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projach.comics_tracker.R
import com.projach.comics_tracker.domain.validation.EmailValidationError
import com.projach.comics_tracker.domain.validation.PasswordValidationError
import com.projach.comics_tracker.domain.validation.UsernameValidationError
import com.projach.comics_tracker.ui.composables.UserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel
import com.projach.comics_tracker.ui.composables.models.FieldType
import com.projach.comics_tracker.ui.theme.Typography
import com.projach.comics_tracker.ui.validation.toStringRes

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
    emailError: EmailValidationError?,
    usernameText: String,
    onUsernameChanged: (String) -> Unit,
    usernameError: UsernameValidationError?,
    passwordText: String,
    onPasswordChanged: (String) -> Unit,
    passwordError: PasswordValidationError?,
    canSubmit: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
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


    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        val minHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 12.dp, vertical = 20.dp)
                .heightIn(min = minHeight - 40.dp)
        ) {
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
                        error = usernameError?.toStringRes()?.let { stringResource(it) },
                        onValueChanged = onUsernameChanged,
                        fieldType = FieldType.Text
                    ),
                    ComicsTestFieldUiModel(
                        text = emailText,
                        label = stringResource(R.string.register_user_email),
                        hint = stringResource(R.string.register_user_email_hint),
                        error = emailError?.toStringRes()?.let { stringResource(it) },
                        onValueChanged = onEmailChanged,
                        fieldType = FieldType.Email
                    ),
                    ComicsTestFieldUiModel(
                        text = passwordText,
                        label = stringResource(R.string.register_user_password),
                        hint = stringResource(R.string.register_user_password_hint),
                        error = passwordError?.toStringRes()?.let { stringResource(it) },
                        onValueChanged = onPasswordChanged,
                        fieldType = FieldType.Password
                    )
                ),
                onSubmit = onSubmit
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    onSubmit.invoke()
                    focusManager.clearFocus()
                },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.register_user_submit))
            }
        }
    }
}