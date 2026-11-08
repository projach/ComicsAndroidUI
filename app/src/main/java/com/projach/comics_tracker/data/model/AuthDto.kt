package com.projach.comics_tracker.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

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
