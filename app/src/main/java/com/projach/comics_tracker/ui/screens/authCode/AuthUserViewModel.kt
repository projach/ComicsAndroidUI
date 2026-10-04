package com.projach.comics_tracker.ui.screens.authCode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.domain.OTP_TIMEOUT_SECONDS
import com.projach.comics_tracker.domain.useCases.UserUseCase
import com.projach.comics_tracker.domain.utils.onError
import com.projach.comics_tracker.domain.utils.onSuccess
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class AuthUserUiState(
    val canResendCode: Boolean = false,
    val showTimer: Boolean = false,
    val timerSeconds: Int = 0,
    val code: String = "",
    val error: String? = null
)

sealed interface AuthUserIntent {
    data class OnCodeFilled(val code: String) : AuthUserIntent
    object RefreshCode : AuthUserIntent
    data class OnCodeChange(val code: String) : AuthUserIntent
}

class AuthUserViewModel(
    val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val email = savedStateHandle.get<String>("email") ?: ""
    private val username = savedStateHandle.get<String>("username") ?: ""
    private val _navigationEvent = Channel<NavigationAction>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(AuthUserUiState())
    val uiState: StateFlow<AuthUserUiState> = _uiState

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            onTimerStart()
        }
    }

    fun onIntent(intent: AuthUserIntent) {
        when (intent) {
            is AuthUserIntent.OnCodeFilled -> onCodeFilled(intent.code)

            AuthUserIntent.RefreshCode -> {
                if (_uiState.value.canResendCode) {
                    onResendCode()
                }
            }

            is AuthUserIntent.OnCodeChange -> {
                _uiState.update { _uiState.value.copy(code = intent.code, error = null) }
            }
        }
    }

    fun onCodeFilled(code: String) {
        viewModelScope.launch {
            userUseCase.verifyUser(email = email, code = code).onSuccess { username ->
                _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(username)))
            }.onSuccess {
                _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(username = username)))
            }.onError {
                _uiState.update { it.copy(code = "", error = "The code is not correct") }
            }
        }
    }

    fun onResendCode() {
        viewModelScope.launch {
            userUseCase.resendCode(email).onSuccess {
                onTimerStart()
            }
        }
    }

    private fun onTimerStart() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showTimer = true,
                    canResendCode = false,
                    timerSeconds = OTP_TIMEOUT_SECONDS
                )
            }
            try {
                (OTP_TIMEOUT_SECONDS - 1 downTo 0).asFlow().onEach { delay(1000L.milliseconds) }
                    .collect { remaining -> _uiState.update { it.copy(timerSeconds = remaining) } }
            } finally {
                _uiState.update { it.copy(showTimer = false, canResendCode = true) }
            }
        }
    }
}