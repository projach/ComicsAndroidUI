package com.projach.comics_tracker.ui.screens.verifyOtp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projach.comics_tracker.R
import com.projach.comics_tracker.ui.composables.ExitDialog
import com.projach.comics_tracker.ui.composables.Loader
import com.projach.comics_tracker.ui.composables.OtpInput
import com.projach.comics_tracker.ui.composables.SuccessCheckAnimation

@Composable
fun OtpScreen(viewModel: OtpViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val remainingMs = viewModel.remainingMs.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        viewModel.onIntent(OtpIntent.ShowExitDialogue(show = true))
    }

    Content(
        otpCode = uiState.value.code,
        timer = { (remainingMs.value / 1000L).toString() },
        showTimer = uiState.value.showTimer,
        canRefresh = uiState.value.canRefreshCode,
        showExitDialog = uiState.value.showExitDialog,
        error = uiState.value.error,
        isLoading = uiState.value.isLoading,
        isSuccess = uiState.value.isSuccess,
        onSubmit = { viewModel.onIntent(OtpIntent.SubmitCode) },
        onRefresh = { viewModel.onIntent(OtpIntent.RefreshCode) },
        onBackPress = { viewModel.onIntent(OtpIntent.GoBack) },
        onOtpCodeChanged = { viewModel.onIntent(OtpIntent.OtpCodeChanged(it)) },
        onExitPress = { viewModel.onIntent(OtpIntent.GoBack) },
        onStayPress = { viewModel.onIntent(OtpIntent.ShowExitDialogue(show = false)) },
        onFinish = { viewModel.onFinish() }
    )
}

@Composable
fun Content(
    otpCode: String,
    timer: () -> String,
    showTimer: Boolean,
    canRefresh: Boolean,
    showExitDialog: Boolean,
    error: String?,
    isLoading: Boolean,
    isSuccess: Boolean,
    onSubmit: () -> Unit,
    onRefresh: () -> Unit,
    onBackPress: () -> Unit,
    onOtpCodeChanged: (String) -> Unit,
    onExitPress: () -> Unit,
    onStayPress: () -> Unit,
    onFinish: () -> Unit
) {
    if (showExitDialog) {
        ExitDialog(
            description = stringResource(R.string.otp_exit_dialog_description),
            exitText = stringResource(R.string.otp_exit_dialog_exit),
            stayText = stringResource(R.string.otp_exit_dialog_stay),
            onExitPress = onExitPress,
            onStayPress = onStayPress
        )
    }

    Loader(isLoading)

    if (isSuccess) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SuccessCheckAnimation {
                onFinish.invoke()
            }
        }
    } else {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val minHeight = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 12.dp, vertical = 20.dp)
                    .heightIn(min = minHeight - 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back_24px),
                    contentDescription = "Back arrow",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable(onClick = onBackPress),
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(30.dp))

                Text(
                    stringResource(R.string.otp_verify_account),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    stringResource(R.string.otp_enter_six_digit_code),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(18.dp))

                OtpInput(
                    modifier = Modifier.fillMaxWidth(),
                    otpCode = otpCode,
                    onOtpCodeChange = onOtpCodeChanged,
                    onOtpComplete = onSubmit,
                    error = error
                )

                Spacer(Modifier.height(25.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showTimer) {
                        Text(
                            "${stringResource(R.string.otp_resend_code_in)} ${timer.invoke()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        TextButton(
                            onClick = onRefresh,
                            modifier = Modifier.clickable(onClick = onRefresh),
                            enabled = canRefresh
                        ) {
                            Text(
                                stringResource(R.string.otp_resend_code),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.otp_continue))
                }
            }
        }
    }
}