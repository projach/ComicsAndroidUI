package com.projach.comics_tracker.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.repository.AuthRepository
import com.projach.comics_tracker.domain.validation.EmailValidationError
import com.projach.comics_tracker.domain.validation.validateEmail
import com.projach.comics_tracker.domain.validation.validatePassword
import com.projach.comics_tracker.domain.validation.validateUsername
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

sealed interface RegisterUserIntent {
    data class EmailChanged(val value: String) : RegisterUserIntent
    data class PasswordChanged(val value: String) : RegisterUserIntent
    data class UsernameChanged(val value: String) : RegisterUserIntent
    object Submit : RegisterUserIntent
    object NavigateToLogin : RegisterUserIntent
}

sealed interface RegisterEffect {
    data class ShowError(val message: String) : RegisterEffect
    data class Navigate(val navigation: NavigationAction) : RegisterEffect
}

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _effect = Channel<RegisterEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _uiState = MutableStateFlow(RegisterUserUiModel())
    val uiState: StateFlow<RegisterUserUiModel> = _uiState

    fun onIntent(intent: RegisterUserIntent) {
        when (intent) {
            is RegisterUserIntent.EmailChanged -> {
                _uiState.update {
                    it.copy(email = intent.value, emailError = validateEmail(intent.value))
                }
            }

            is RegisterUserIntent.PasswordChanged -> {
                _uiState.update {
                    it.copy(password = intent.value, passwordError = validatePassword(intent.value))
                }
            }

            is RegisterUserIntent.UsernameChanged -> {
                _uiState.update {
                    it.copy(username = intent.value, usernameError = validateUsername(intent.value))
                }
            }

            RegisterUserIntent.NavigateToLogin -> {
                viewModelScope.launch {
                    _effect.send(RegisterEffect.Navigate(NavigationAction.NavigateTo(Screen.LoginUserScreen())))
                }
            }

            RegisterUserIntent.Submit -> {
                if (_uiState.value.formIsValid) {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.register(
                            email = _uiState.value.email,
                            password = _uiState.value.password,
                            username = _uiState.value.username
                        ).onSuccess {
                            Logger.d(
                                "RegisterUserViewModel",
                                "Successful register navigating to login screen"
                            )
                            _effect.send(
                                RegisterEffect.Navigate(
                                    NavigationAction.NavigateTo(
                                        Screen.OtpScreen(
                                            email = _uiState.value.email
                                        )
                                    )
                                )
                            )
                        }.onError { error ->
                            Logger.e("RegisterUserViewModel", "Error on register with error $error")
                            _uiState.update { it.copy(isLoading = false) }
                            when (error) {
                                is DataError.Conflict -> {
                                    when {
                                        error.message.lowercase()
                                            .contains("email") -> _uiState.update {
                                            it.copy(emailError = EmailValidationError.Conflict)
                                        }

                                        error.message.lowercase()
                                            .contains("username") -> _uiState.update {
                                            it.copy(emailError = EmailValidationError.Conflict)
                                        }

                                        else -> {
                                            _effect.send(RegisterEffect.ShowError("Email or Username already exists"))
                                        }
                                    }
                                }

                                DataError.Network, DataError.NoInternet, DataError.Timeout -> _effect.send(
                                    RegisterEffect.ShowError("There is a connection issue please try again")
                                )

                                is DataError.TooManyRequests -> _effect.send(
                                    RegisterEffect.ShowError(
                                        "You need to wait for ${error.retryAfterRemainingSeconds} to try again with this email/username"
                                    )
                                )

                                is DataError.Unknown -> {
                                    Logger.e("RegisterUserViewModel", "Unknown error in register call ${error.cause}")
                                    _effect.send(
                                        RegisterEffect.ShowError(
                                            "There was an error please try again!"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}