package com.projach.comics_tracker.data.session

import com.projach.comics_tracker.data.security.JwtUtils
import com.projach.comics_tracker.data.security.TokenStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class SessionEndReason { LoggedOut, Unauthorized }

@Singleton
class SessionManager @Inject constructor(private val tokenStorage: TokenStorage) {
    private val _sessionEnded = Channel<SessionEndReason>(Channel.CONFLATED)
    val sessionEnded = _sessionEnded.receiveAsFlow()

    fun isLoggedIn(): Boolean {
        val token = tokenStorage.getToken()
        return !token.isNullOrBlank() && !JwtUtils.isExpired(token)
    }

    fun getUsername(): String? =
        tokenStorage.getToken()?.let { JwtUtils.getClaim(it, "sub") }

    fun onLoggedIn(token: String) = tokenStorage.saveToken(token)

    fun logout() = endSession(SessionEndReason.LoggedOut)

    fun onUnauthorized() = endSession(SessionEndReason.Unauthorized)

    private fun endSession(reason: SessionEndReason) {
        tokenStorage.clearToken()
        _sessionEnded.trySend(reason)
    }
}