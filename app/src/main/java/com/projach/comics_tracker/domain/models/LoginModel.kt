package com.projach.comics_tracker.domain.models

import kotlin.time.Instant

data class LoginModel(
    val token: String,
    val expiresAt: Instant
)
