package com.projach.comics_tracker.ui.screens.authCode

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.OtpInputField

@Composable
fun AuthUserScreen(viewModel: AuthUserViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        onCodeComplete = { viewModel.onIntent(AuthUserIntent.OnCodeFilled(it)) },
        resendCode = { viewModel.onIntent(AuthUserIntent.RefreshCode) },
        onCodeChange = { viewModel.onIntent(AuthUserIntent.OnCodeChange(it)) },
        timerSeconds = uiState.value.timerSeconds,
        code = uiState.value.code,
        canResendCode = uiState.value.canResendCode,
        showTimer = uiState.value.showTimer,
        error = uiState.value.error
    )
}

@Composable
fun Content(
    onCodeComplete: (String) -> Unit,
    resendCode: () -> Unit,
    onCodeChange: (String) -> Unit,
    timerSeconds: Int,
    code: String,
    canResendCode: Boolean,
    showTimer: Boolean,
    error: String?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.auth_info),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OtpInputField(
                onCodeComplete = onCodeComplete,
                value = code,
                onValueChange = onCodeChange,
                error = error
            )
            if (showTimer) {
                Spacer(Modifier.size(26.dp))
                Text("You have to wait $timerSeconds seconds to request new code")
            }
            if (canResendCode) {
                TextButton(
                    onClick = resendCode
                ) {
                    Text(
                        stringResource(R.string.resend_otp_code)
                    )
                }
            }
        }
    }
}