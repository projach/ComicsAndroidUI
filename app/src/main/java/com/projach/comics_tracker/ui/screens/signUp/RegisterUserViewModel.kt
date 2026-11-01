package com.projach.comics_tracker.ui.screens.signUp

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.data.source.remote.apis.AuthApi
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
                    it.copy(email = intent.value)
                }
            }
            is RegisterUserIntent.PasswordChanged -> {
                _uiState.update {
                    it.copy(password = intent.value)
                }
            }
            is RegisterUserIntent.UsernameChanged -> {
                _uiState.update {
                    it.copy(username = intent.value)
                }
            }
            RegisterUserIntent.NavigateToLogin -> {
                viewModelScope.launch {
                    _navigationEvent.send(NavigationAction.NavigateTo(Screen.LoginUserScreen()))
                }
            }
            RegisterUserIntent.Submit -> {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.register(
                        email = _uiState.value.email,
                        password = _uiState.value.password,
                        username = _uiState.value.username
                    )
                }
            }
        }
    }

}