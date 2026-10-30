package com.projach.comics_tracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val username: String, val password: String)

@Serializable
data class SignUpRequestDto(val username: String, val password: String, val email: String)
