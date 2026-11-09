package com.projach.comics_tracker.data.repository

import com.projach.comics_tracker.data.model.LogInRequestDto
import com.projach.comics_tracker.data.model.RefreshOtpResponseDto
import com.projach.comics_tracker.data.model.ResendOtpRequestDto
import com.projach.comics_tracker.data.model.SignUpRequestDto
import com.projach.comics_tracker.data.model.UserDto
import com.projach.comics_tracker.data.model.VerifyRequestDto
import com.projach.comics_tracker.data.source.remote.apis.AuthApi
import com.projach.comics_tracker.data.source.remote.mappers.safeApiCall
import com.projach.comics_tracker.domain.dataResult.Conflict
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.dataResult.InvalidOrExpiredCode
import com.projach.comics_tracker.domain.dataResult.InvalidUsernameOrPassword
import com.projach.comics_tracker.domain.dataResult.LoginUserDataError
import com.projach.comics_tracker.domain.dataResult.NotFound
import com.projach.comics_tracker.domain.dataResult.RefreshOtpCodeDataError
import com.projach.comics_tracker.domain.dataResult.RegisterUserDataError
import com.projach.comics_tracker.domain.dataResult.TooManyRequests
import com.projach.comics_tracker.domain.dataResult.VerifyOtpDataError
import com.projach.comics_tracker.domain.dataResult.map
import com.projach.comics_tracker.domain.models.LoginModel
import com.projach.comics_tracker.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): DataResult<Unit, RegisterUserDataError> =
        safeApiCall(
            commonError = { it },
            httpError = { code, message, headers ->
                when (code) {
                    409 -> Conflict(message)
                    429 -> TooManyRequests(headers["Retry-After"]?.toIntOrNull())
                    else -> null
                }
            },
            call = {
                authApi.registerUser(
                    SignUpRequestDto(
                        username = username,
                        password = password,
                        email = email
                    )
                )
            }
        )

    override suspend fun verify(
        email: String,
        code: String
    ): DataResult<UserDto, VerifyOtpDataError> =
        safeApiCall(
            commonError = { it },
            httpError = { code, message, _ ->
                when (code) {
                    409 -> Conflict(message)
                    400 -> InvalidOrExpiredCode
                    else -> null
                }
            },
            call = {
                authApi.verifyUser(
                    VerifyRequestDto(
                        email = email,
                        code = code
                    )
                )
            }
        )

    override suspend fun refreshOtp(email: String): DataResult<RefreshOtpResponseDto, RefreshOtpCodeDataError> =
        safeApiCall(
            commonError = { it },
            httpError = { code, _, headers ->
                when (code) {
                    404 -> NotFound
                    429 -> TooManyRequests(headers["Retry-After"]?.toIntOrNull())
                    else -> null
                }
            },
            call = {
                authApi.resendOtp(
                    ResendOtpRequestDto(
                        email = email
                    )
                )
            }
        )

    override suspend fun login(
        username: String,
        password: String
    ): DataResult<LoginModel, LoginUserDataError> =
        safeApiCall(
            commonError = { it },
            httpError = { code, _, _ ->
                when (code) {
                    401 -> InvalidUsernameOrPassword
                    else -> null
                }
            },
            call = { authApi.loginUser(LogInRequestDto(username = username, password = password)) }
        ).map {
            it.toModel()
        }
}