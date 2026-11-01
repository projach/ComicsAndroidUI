package com.projach.comics_tracker.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.repository.AuthRepository
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

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _navigationEvent = Channel<NavigationAction>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(RegisterUserUiModel())
    val uiState: StateFlow<RegisterUserUiModel> = _uiState

    fun onIntent(intent: RegisterUserIntent){
        when(intent){
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
                    _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen()))
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
                            Logger.d("RegisterUserViewModel", "Successful register navigating to login screen")
                            _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen(username = _uiState.value.username)))
                        }.onError { error ->
                            Logger.e("RegisterUserViewModel", "Error on register with error $error")
                            _uiState.update { it.copy(isLoading = false) }
//                            when(error){
//                                is DataError.Conflict -> TODO()
//                                DataError.Network -> TODO()
//                                DataError.NoInternet -> TODO()
//                                DataError.Timeout -> TODO()
//                                is DataError.TooManyRequests -> TODO()
//                                is DataError.Unknown -> TODO()
//                            }
                        }
                    }
                }
            }
        }
    }

}