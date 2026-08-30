package com.projach.comics_tracker.data.models.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val username: String,
    val password: String,
    val email: String
)


@Serializable
data class LoginUserDto(
    val username: String,
    val password: String
)

@Serializable
data class RegisterUserResponseDto(
    val username: String,
    val email: String
)

@Serializable
data class LoginUserResponseDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_at")
    val expiresAt: String
)
