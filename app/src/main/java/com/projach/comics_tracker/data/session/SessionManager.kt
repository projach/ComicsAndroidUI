package com.projach.comics_tracker.data.session

import com.projach.comics_tracker.data.security.JwtUtils
import com.projach.comics_tracker.data.security.TokenStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val tokenStorage: TokenStorage) {
    private val _unauthorized = Channel<Unit>(Channel.CONFLATED)
    val unauthorizedEvent: Flow<Unit> = _unauthorized.receiveAsFlow()

    fun isLoggedIn(): Boolean {
        val token = tokenStorage.getToken()
        return !token.isNullOrBlank() && !JwtUtils.isExpired(token)
    }

    fun getUsername(): String? =
        tokenStorage.getToken()?.let { JwtUtils.getClaim(it, "sub") }

    fun onLoggedIn(token: String) = tokenStorage.saveToken(token)

    fun logout() = tokenStorage.clearToken()

    fun onUnauthorized() {
        tokenStorage.clearToken()
        _unauthorized.trySend(Unit)
    }
}