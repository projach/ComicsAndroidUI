package com.projach.comics_tracker.ui.screens.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.domain.dataResult.InvalidUsernameOrPassword
import com.projach.comics_tracker.domain.dataResult.NetworkUserDataError
import com.projach.comics_tracker.domain.dataResult.ServerUserDataError
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.repository.AuthRepository
import com.projach.comics_tracker.domain.validation.validatePassword
import com.projach.comics_tracker.domain.validation.validateUsername
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.screens.login.LoginUserEffect.Navigate
import com.projach.comics_tracker.ui.screens.login.LoginUserEffect.ShowError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface LoginUserIntent {
    data class OnUsernameChanged(val value: String) : LoginUserIntent
    data class OnPasswordChanged(val value: String) : LoginUserIntent
    object OnSubmit : LoginUserIntent
    object GoToRegister : LoginUserIntent
}

sealed interface LoginUserEffect {
    data class Navigate(val navigation: NavigationAction) : LoginUserEffect
    data class ShowError(val message: String) : LoginUserEffect
}

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUserScreenUiModel())
    val uiState: StateFlow<LoginUserScreenUiModel> = _uiState

    private val _effect = Channel<LoginUserEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        savedStateHandle.toRoute<Screen.LoginUserScreen>().username?.let { username ->
            _uiState.update {
                it.copy(username = username)
            }
        }
    }

    fun onIntent(intent: LoginUserIntent) {
        when (intent) {
            is LoginUserIntent.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = intent.value,
                        passwordError = validatePassword(intent.value),
                        error = null
                    )
                }
            }

            is LoginUserIntent.OnUsernameChanged -> {
                _uiState.update {
                    it.copy(
                        username = intent.value,
                        usernameError = validateUsername(intent.value),
                        error = null
                    )
                }
            }

            LoginUserIntent.OnSubmit -> onSubmit()

            LoginUserIntent.GoToRegister -> goToRegister()
        }
    }

    private fun goToRegister() {
        viewModelScope.launch {
            _effect.send(
                Navigate(
                    NavigationAction.NavigateToAndClearBackStack(
                        Screen.RegisterUserScreen
                    )
                )
            )
        }
    }

    private fun onSubmit() {
        if (_uiState.value.formIsValid) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO) {
                authRepository.login(
                    username = _uiState.value.username,
                    password = _uiState.value.password
                ).onSuccess { result ->
                    //TODO(add check after token save when the app starts if the token is alive to bypass the login/register phase)
                    //save token for the time it is active in secure storage
                    //navigate to comic screen!!
                    _effect.send(
                        Navigate(
                            NavigationAction.NavigateToAndClearBackStack(
                                Screen.ComicsListScreen(_uiState.value.username)
                            )
                        )
                    )
                }.onError { error ->
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    when (error) {
                        NetworkUserDataError.NoInternet,
                        NetworkUserDataError.Timeout,
                        ServerUserDataError.Internal,
                        ServerUserDataError.Unavailable -> {
                            _effect.send(ShowError("Please try again there was a network error"))
                        }

                        is ServerUserDataError.Unknown -> {
                            Logger.e("LOGIN_USER_VIEWMODEL", "We got error with code ${error.code}")
                            _effect.send(ShowError("Please try again there was a network error"))
                        }

                        InvalidUsernameOrPassword -> {
                            _uiState.update {
                                it.copy(error = "Invalid username or password")
                            }
                        }
                    }
                }
            }
        }
    }
}