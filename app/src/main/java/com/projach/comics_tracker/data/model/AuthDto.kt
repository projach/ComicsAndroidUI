package com.projach.comics_tracker.data.model

import com.google.gson.annotations.SerializedName
import com.projach.comics_tracker.domain.models.LoginModel
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class UserDto(val username: String, val password: String)

@Serializable
data class RefreshOtpResponseDto(
    @SerializedName("Retry-After") val retryAfterSeconds: Int
)

@Serializable
data class SignUpRequestDto(val username: String, val password: String, val email: String)

@Serializable
data class VerifyRequestDto(val email: String, val code: String)

@Serializable
data class ResendOtpRequestDto(val email: String)

@Serializable
data class LogInRequestDto(val username: String, val password: String)

@Serializable
data class LoginResponseDto(
    @SerializedName("access_token") val token: String,
    @SerializedName("expires_at") val expiresAt: String
) {
    fun toModel(): LoginModel = LoginModel(token, Instant.parse(expiresAt))
}