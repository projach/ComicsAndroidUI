package com.projach.comics_tracker.ui.screens.registerUser

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.domain.useCases.UserUseCase
import com.projach.comics_tracker.domain.utils.NetworkError
import com.projach.comics_tracker.domain.utils.Validators
import com.projach.comics_tracker.domain.utils.onError
import com.projach.comics_tracker.domain.utils.onSuccess
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _registerState = MutableStateFlow<String?>("")
    val registerState: StateFlow<String?> = _registerState

    private val _navigationEvent = Channel<NavigationAction>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    val emailState = TextFieldState()
    val passwordState = TextFieldState()
    val usernameState = TextFieldState()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError.asStateFlow()

    private val fieldsNotEmpty: Flow<Boolean> = combine(
        snapshotFlow { emailState.text.toString() },
        snapshotFlow { passwordState.text.toString() },
        snapshotFlow { usernameState.text.toString() }
    ) { email, password, username ->
        email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()
    }

    private val noErrors: Flow<Boolean> = combine(
        emailError,
        passwordError,
        usernameError
    ) { emailErr, passwordErr, usernameErr ->
        emailErr == null && passwordErr == null && usernameErr == null
    }

    val isFormValid: StateFlow<Boolean> = combine(fieldsNotEmpty, noErrors) { notEmpty, valid ->
        notEmpty && valid
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            merge(
                snapshotFlow { emailState.text.toString() }.map { validateEmail(it) to "email" },
                snapshotFlow { passwordState.text.toString() }.map { validatePassword(it) to "password" },
                snapshotFlow { usernameState.text.toString() }.map { validateUsername(it) to "username" }
            ).collect { (error, field) ->
                when (field) {
                    "email" -> _emailError.update {
                        error
                    }

                    "password" -> _passwordError.update {
                        error
                    }

                    "username" -> _usernameError.update {
                        error
                    }
                }
            }
        }
    }

    fun onRegisterUserClick(username: String, email: String, password: String) {
        viewModelScope.launch {
            _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(null)))
            val result = userUseCase.registerUser(
                username = username,
                email = email,
                password = password
            )
            result.onError { error ->
                _registerState.update {
                    when (error) {
                        NetworkError.REQUEST_TIMEOUT -> TODO()
                        NetworkError.UNAUTHORIZED -> TODO()
                        NetworkError.CONFLICT -> TODO()
                        NetworkError.TOO_MANY_REQUESTS -> TODO()
                        NetworkError.NO_INTERNET -> TODO()
                        NetworkError.PAYLOAD_TOO_LARGE -> TODO()
                        NetworkError.SERVER_ERROR -> TODO()
                        NetworkError.SERIALIZATION -> TODO()
                        NetworkError.UNKNOWN -> "There was an error try again"
                    }
                }
            }.onSuccess { username ->
                _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(username)))
            }
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(null)))
        }
    }

    private fun validateEmail(email: String): String? = when {
        email.isEmpty() -> null
        !Validators.isValidEmail(email) -> "Enter a valid email"
        else -> null
    }

    private fun validateUsername(username: String): String? = when {
        username.isEmpty() -> null
        username.length !in 4..14 -> "Username needs to be 4 to 14 characters"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.isEmpty() -> null
        password.length < 8 -> "Password needs more than 8 characters"
        else -> null
    }
}