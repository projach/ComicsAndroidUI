package com.projach.comics_tracker.domain.useCases

import com.projach.comics_tracker.data.models.users.RegisterUserDto
import com.projach.comics_tracker.data.models.users.ResendCodeDto
import com.projach.comics_tracker.data.models.users.VerifyUserDto
import com.projach.comics_tracker.data.source.remote.UserClient
import com.projach.comics_tracker.domain.utils.NetworkError
import com.projach.comics_tracker.domain.utils.Result

class UserUseCase(private val userClient: UserClient) {
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<String, NetworkError> =
        userClient.registerUser(
            RegisterUserDto(
                username = username,
                password = password,
                email = email
            )
        )

    suspend fun verifyUser(
        email: String,
        code: String
    ): Result<String, NetworkError> =
        userClient.verifyUser(VerifyUserDto(email = email, code = code))

    suspend fun resendCode(email: String): Result<String, NetworkError> =
        userClient.resendCode(ResendCodeDto(email))
}