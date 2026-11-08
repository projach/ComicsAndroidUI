package com.projach.comics_tracker.ui.screens.verifyOtp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.domain.OTP_TIMER_MS
import com.projach.comics_tracker.domain.dataResult.Conflict
import com.projach.comics_tracker.domain.dataResult.InvalidOrExpiredCode
import com.projach.comics_tracker.domain.dataResult.NetworkUserDataError
import com.projach.comics_tracker.domain.dataResult.NotFound
import com.projach.comics_tracker.domain.dataResult.ServerUserDataError
import com.projach.comics_tracker.domain.dataResult.TooManyRequests
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.repository.AuthRepository
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OtpIntent {
    data class OtpCodeChanged(val code: String) : OtpIntent
    object RefreshCode : OtpIntent
    object SubmitCode : OtpIntent
    object GoBack : OtpIntent
    data class ShowExitDialogue(val show: Boolean) : OtpIntent
}

sealed interface VerifyOtpEffect {
    data class Navigate(val navigationAction: NavigationAction) : VerifyOtpEffect
    data class ShowError(val message: String) : VerifyOtpEffect
}

@HiltViewModel
class OtpViewModel @Inject constructor(
    val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyOtpUiModel())
    val uiState: StateFlow<VerifyOtpUiModel> = _uiState

    private val timer = TimerHelper(
        scope = viewModelScope,
        handle = savedStateHandle,
        onFinished = {
            _uiState.update {
                it.copy(canRefreshCode = true, showTimer = false)
            }
        }
    )
    val remainingMs = timer.remainingMs

    private val _effect = Channel<VerifyOtpEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    private val routeState = savedStateHandle.toRoute<Screen.OtpScreen>()
    private val email = routeState.email
    private val username = routeState.username

    init {
        startTimer()
    }

    fun onIntent(intent: OtpIntent) {
        when (intent) {
            is OtpIntent.OtpCodeChanged -> {
                _uiState.update {
                    it.copy(code = intent.code, error = null)
                }
            }

            OtpIntent.RefreshCode -> refreshCode()

            OtpIntent.SubmitCode -> submitCode()
            OtpIntent.GoBack -> {
                viewModelScope.launch {
                    _effect.send(
                        VerifyOtpEffect.Navigate(
                            NavigationAction.NavigateToAndClearBackStack(
                                Screen.RegisterUserScreen
                            )
                        )
                    )
                }
            }

            is OtpIntent.ShowExitDialogue -> {
                _uiState.update {
                    it.copy(showExitDialog = intent.show)
                }
            }
        }
    }

    private fun refreshCode() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.refreshOtp(email).onSuccess {
                startTimer()
            }.onError { error ->
                when (error) {
                    NetworkUserDataError.NoInternet,
                    NetworkUserDataError.Timeout,
                    ServerUserDataError.Unavailable,
                    ServerUserDataError.Internal -> {
                        _effect.send(VerifyOtpEffect.ShowError("There was a network error please try again"))
                    }

                    is ServerUserDataError.Unknown -> {
                        Logger.e(
                            "VERIFY_OTP_VIEWMODEL",
                            "we got unknown error with code: ${error.code}"
                        )
                        _effect.send(VerifyOtpEffect.ShowError("There was a network error please try again"))
                    }

                    NotFound -> {
                        _effect.send(VerifyOtpEffect.ShowError("Otp code is expired please register again"))
                        _effect.send(
                            VerifyOtpEffect.Navigate(
                                NavigationAction.NavigateToAndClearBackStack(
                                    Screen.RegisterUserScreen
                                )
                            )
                        )
                    }

                    is TooManyRequests -> {
                        //should not happen this is in case timer calculator is off by some seconds and the user is so fast to press resend code
                        _effect.send(VerifyOtpEffect.ShowError("Please try again"))
                    }
                }
            }
        }
    }

    private fun submitCode() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.verify(
                email = email,
                code = _uiState.value.code
            ).onSuccess {
                _uiState.update {
                    VerifyOtpUiModel(isSuccess = true)
                }
            }.onError { error ->
                _uiState.update {
                    it.copy(code = "", isLoading = false)
                }
                when (error) {
                    NetworkUserDataError.NoInternet,
                    NetworkUserDataError.Timeout,
                    ServerUserDataError.Unavailable,
                    ServerUserDataError.Internal -> {
                        _effect.send(VerifyOtpEffect.ShowError("There was a network error please try again"))
                    }

                    is ServerUserDataError.Unknown -> {
                        Logger.e(
                            "VERIFY_OTP_VIEWMODEL",
                            "we got unknown error with code: ${error.code}"
                        )
                        _effect.send(VerifyOtpEffect.ShowError("There was a network error please try again"))
                    }

                    is Conflict -> {
                        _effect.send(VerifyOtpEffect.ShowError("User with these credentials already exists"))
                        _effect.send(
                            VerifyOtpEffect.Navigate(
                                NavigationAction.NavigateToAndClearBackStack(
                                    Screen.RegisterUserScreen
                                )
                            )
                        )
                    }

                    InvalidOrExpiredCode -> {
                        _uiState.update {
                            it.copy(error = "Invalid or expired code")
                        }
                    }
                }
            }
        }
    }

    fun onFinish() {
        viewModelScope.launch {
            _effect.send(
                VerifyOtpEffect.Navigate(
                    NavigationAction.NavigateToAndClearBackStack(
                        Screen.LoginUserScreen(
                            username = username
                        )
                    )
                )
            )
        }
    }

    private fun startTimer() {
        _uiState.update {
            it.copy(canRefreshCode = false, showTimer = true)
        }
        timer.start(OTP_TIMER_MS)
    }
}