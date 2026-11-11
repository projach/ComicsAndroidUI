package com.projach.comics_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.data.session.SessionManager
import com.projach.comics_tracker.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _startDestination = MutableStateFlow<Screen?>(null)
    val startDestination: StateFlow<Screen?> = _startDestination

    val sessionEndedEvent = sessionManager.sessionEnded

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val username = sessionManager.getUsername()
            _startDestination.update {
                if (sessionManager.isLoggedIn() && username != null) {
                    Screen.ComicsListScreen(username)
                } else {
                    Screen.LoginUserScreen()
                }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        sessionManager.logout()
        onLogout.invoke()
    }
}