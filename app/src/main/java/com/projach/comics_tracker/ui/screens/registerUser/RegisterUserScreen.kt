package com.projach.comics_tracker.ui.screens.registerUser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.projach.comics_tracker.R


@Composable
fun RegisterUserScreen(viewModel: RegisterUserViewModel) {

    val isFormValid = viewModel.isFormValid.collectAsState()
    val emailError = viewModel.emailError.collectAsState()
    val passwordError = viewModel.passwordError.collectAsState()
    val usernameError = viewModel.usernameError.collectAsState()

    Content(
        onRegisterClick = { username, password, email ->
            viewModel.onRegisterUserClick(
                username,
                email,
                password
            )
        },
        onLoginClick = { viewModel.onLoginClick() },
        isFormValid = isFormValid.value,
        emailState = viewModel.emailState,
        passwordState = viewModel.passwordState,
        usernameState = viewModel.usernameState,
        emailError = emailError.value,
        passwordError = passwordError.value,
        usernameError = usernameError.value
    )
}

@Composable
fun Content(
    onRegisterClick: (username: String, password: String, email: String) -> Unit,
    onLoginClick: () -> Unit,
    isFormValid: Boolean,
    emailState: TextFieldState,
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    emailError: String?,
    usernameError: String?,
    passwordError: String?
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                state = emailState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.register_email)) },
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it)
                    }
                }
            )
            OutlinedTextField(
                state = usernameState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.register_username)) },
                isError = usernameError != null,
                supportingText = {
                    usernameError?.let {
                        Text(it)
                    }
                }
            )
            OutlinedSecureTextField(
                state = passwordState,
                label = { Text(stringResource(R.string.register_password)) },
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let {
                        Text(it)
                    }
                }
            )
            Text(
                text = stringResource(R.string.register_login_navigation_btn),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = onLoginClick)
            )
            Button(
                onClick = {
                    onRegisterClick.invoke(
                        usernameState.text.toString(),
                        passwordState.text.toString(),
                        emailState.text.toString()
                    )
                },
                enabled = isFormValid
            ) {
                Text(stringResource(R.string.register_btn))
            }
        }
    }
}