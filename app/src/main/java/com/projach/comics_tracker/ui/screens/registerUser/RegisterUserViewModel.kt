package com.projach.comics_tracker.ui.screens.registerUser

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.domain.useCases.UserUseCase
import com.projach.comics_tracker.domain.utils.Logger
import com.projach.comics_tracker.domain.utils.NetworkError
import com.projach.comics_tracker.domain.utils.Validators.validateEmail
import com.projach.comics_tracker.domain.utils.Validators.validatePassword
import com.projach.comics_tracker.domain.utils.Validators.validateUsername
import com.projach.comics_tracker.domain.utils.onError
import com.projach.comics_tracker.domain.utils.onSuccess
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    private val _navigationEvent = Channel<NavigationAction>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    val emailState = TextFieldState()
    val passwordState = TextFieldState()
    val usernameState = TextFieldState()

    private val _uiState = MutableStateFlow(RegisterUserUiState())
    val uiState: StateFlow<RegisterUserUiState> = _uiState

    init {
        viewModelScope.launch {
            merge(
                snapshotFlow { emailState.text.toString() }.map { validateEmail(it) to "email" },
                snapshotFlow { passwordState.text.toString() }.map { validatePassword(it) to "password" },
                snapshotFlow { usernameState.text.toString() }.map { validateUsername(it) to "username" }
            ).collect { (error, field) ->
                when (field) {
                    "email" -> _uiState.update {
                        _uiState.value.copy(emailError = error)
                    }

                    "password" -> _uiState.update {
                        _uiState.value.copy(passwordError = error)
                    }

                    "username" -> _uiState.update {
                        _uiState.value.copy(usernameError = error)
                    }
                }
            }
        }
    }

    fun isFormValid(): Boolean {
        return emailState.text.toString().isNotEmpty()
                && passwordState.text.toString().isNotEmpty()
                && usernameState.text.toString().isNotEmpty()
                && _uiState.value.emailError == null
                && _uiState.value.passwordError == null
                && _uiState.value.usernameError == null
    }

    fun onRegisterUserClick(username: String, email: String, password: String) {
        _uiState.update {
            _uiState.value.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = userUseCase.registerUser(
                username = username,
                email = email,
                password = password
            )
            result.onError { error ->
                Logger.d("REGISTER-USER-VIEW-MODEL", "we have error $error")
                _errorEvent.emit(
                    when (error) {
                        NetworkError.REQUEST_TIMEOUT -> "Please try again"
                        NetworkError.UNAUTHORIZED -> "You are not authorized!"
                        NetworkError.NO_INTERNET -> "You are not connected to the internet please try again"
                        else -> "There was an error please try again"
                    }
                )
                _uiState.update {
                    _uiState.value.copy(
                        isLoading = false
                    )
                }
            }.onSuccess { _ ->
                _uiState.update {
                    _uiState.value.copy(isLoading = false)
                }
                _navigationEvent.send(NavigationAction.NavigateTo(Screen.AuthUserScreen(email)))
            }
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(null)))
        }
    }
}