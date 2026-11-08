package com.projach.comics_tracker.domain.repository

import com.projach.comics_tracker.data.model.RefreshOtpResponseDto
import com.projach.comics_tracker.data.model.UserDto
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.dataResult.RefreshOtpCodeDataError
import com.projach.comics_tracker.domain.dataResult.RegisterUserDataError
import com.projach.comics_tracker.domain.dataResult.VerifyOtpDataError


interface AuthRepository {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): DataResult<Unit, RegisterUserDataError>

    suspend fun verify(email: String, code: String): DataResult<UserDto, VerifyOtpDataError>
    suspend fun refreshOtp(email: String): DataResult<RefreshOtpResponseDto, RefreshOtpCodeDataError>
}