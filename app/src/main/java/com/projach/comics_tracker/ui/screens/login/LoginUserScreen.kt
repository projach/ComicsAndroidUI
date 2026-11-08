package com.projach.comics_tracker.ui.screens.login

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projach.comics_tracker.R
import com.projach.comics_tracker.domain.validation.PasswordValidationError
import com.projach.comics_tracker.domain.validation.UsernameValidationError
import com.projach.comics_tracker.ui.composables.Loader
import com.projach.comics_tracker.ui.composables.UserForm
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel
import com.projach.comics_tracker.ui.composables.models.FieldType
import com.projach.comics_tracker.ui.validation.toStringRes

@Composable
fun LoginUserScreen(viewModel: LoginUserViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        usernameText = uiState.value.username,
        onUsernameChanged = { viewModel.onIntent(LoginUserIntent.OnUsernameChanged(it)) },
        usernameError = uiState.value.usernameError,
        passwordText = uiState.value.password,
        onPasswordChanged = { viewModel.onIntent(LoginUserIntent.OnPasswordChanged(it)) },
        passwordError = uiState.value.passwordError,
        canSubmit = uiState.value.formIsValid,
        isLoading = uiState.value.isLoading,
        error = uiState.value.error,
        onSubmit = { viewModel.onIntent(LoginUserIntent.OnSubmit) },
        onGoToRegister = { viewModel.onIntent(LoginUserIntent.GoToRegister) }
    )
}

@Composable
fun Content(
    usernameText: String,
    onUsernameChanged: (String) -> Unit,
    usernameError: UsernameValidationError?,
    passwordText: String,
    onPasswordChanged: (String) -> Unit,
    passwordError: PasswordValidationError?,
    canSubmit: Boolean,
    isLoading: Boolean,
    error: String?,
    onSubmit: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Loader(isLoading)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
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
                text = stringResource(R.string.login_user_welcome_back),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_user_log_in_to_account),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            UserForm(
                fields = listOf(
                    ComicsTestFieldUiModel(
                        text = usernameText,
                        label = stringResource(R.string.login_user_username_label),
                        hint = stringResource(R.string.login_user_username_hint),
                        error = usernameError?.toStringRes()?.let { stringResource(it) },
                        onValueChanged = onUsernameChanged,
                        fieldType = FieldType.Text
                    ),
                    ComicsTestFieldUiModel(
                        text = passwordText,
                        label = stringResource(R.string.login_user_password_label),
                        hint = stringResource(R.string.login_user_password_hint),
                        error = passwordError?.toStringRes()?.let { stringResource(it) },
                        onValueChanged = onPasswordChanged,
                        fieldType = FieldType.Password
                    )
                ),
                onSubmit = onSubmit,
                error = error
            )

            Spacer(Modifier.height(4.dp))



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
                Text(stringResource(R.string.login_user_log_in))
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Text(
                    text = stringResource(R.string.login_user_or),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.login_user_dont_have_account_yet))
                Spacer(Modifier.width(2.dp))
                TextButton(
                    onClick = onGoToRegister,
                ) {
                    Text(stringResource(R.string.login_user_create_account))
                }
            }
        }
    }
}