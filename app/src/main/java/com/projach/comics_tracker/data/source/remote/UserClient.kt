package com.projach.comics_tracker.data.source.remote

import com.projach.comics_tracker.data.models.users.LoginUserDto
import com.projach.comics_tracker.data.models.users.LoginUserResponseDto
import com.projach.comics_tracker.data.models.users.RegisterUserDto
import com.projach.comics_tracker.data.models.users.RegisterUserResponseDto
import com.projach.comics_tracker.data.models.users.ResendCodeDto
import com.projach.comics_tracker.data.models.users.VerifyUserDto
import com.projach.comics_tracker.domain.LOGIN_ENDPOINT
import com.projach.comics_tracker.domain.REGISTER_ENDPOINT
import com.projach.comics_tracker.domain.RESEND_CODE_ENDPOINT
import com.projach.comics_tracker.domain.VERIFY_ENDPOINT
import com.projach.comics_tracker.domain.utils.NetworkError
import com.projach.comics_tracker.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException

class UserClient(
    private val httpClient: HttpClient
) {
    suspend fun registerUser(user: RegisterUserDto): Result<String, NetworkError> {
        val result = try {
            httpClient.post(REGISTER_ENDPOINT) {
                setBody(user)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NoInternet)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.Serialization)
        } catch (e: SocketTimeoutException) {
            return Result.Error(NetworkError.RequestTimeout)
        } catch (e: Exception) {
            return Result.Error(NetworkError.Unknown)
        }

        return when (result.status.value) {
            in 200..299 -> {
                Result.Success("")
            }
//            401 -> Result.Error(NetworkError.Unathorized) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.Conflict)
            408 -> Result.Error(NetworkError.RequestTimeout)
            in 500..599 -> Result.Error(NetworkError.ServerError)
            else -> Result.Error(NetworkError.Unknown)
        }
    }

    suspend fun verifyUser(user: VerifyUserDto): Result<String, NetworkError> {
        val result = try {
            httpClient.post(VERIFY_ENDPOINT) {
                setBody(user)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NoInternet)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.Serialization)
        } catch (e: SocketTimeoutException) {
            return Result.Error(NetworkError.RequestTimeout)
        } catch (e: Exception) {
            return Result.Error(NetworkError.Unknown)
        }

        return when (result.status.value) {
            in 200..299 -> {
                val user = result.body<RegisterUserResponseDto>()
                Result.Success(user.username)
            }
//            401 -> Result.Error(NetworkError.Unathorized) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.Conflict)
            408 -> Result.Error(NetworkError.RequestTimeout)
            429 -> {
                Result.Error(NetworkError.OtpTimeout)
            }

            in 500..599 -> Result.Error(NetworkError.ServerError)
            else -> Result.Error(NetworkError.Unknown)
        }
    }

    suspend fun resendCode(email: ResendCodeDto): Result<String, NetworkError> {
        val result = try {
            httpClient.post(RESEND_CODE_ENDPOINT) {
                setBody(email)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NoInternet)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.Serialization)
        }

        return when (result.status.value) {
            in 200..299 -> {
                Result.Success("")
            }
//            401 -> Result.Error(NetworkError.Unathorized) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.Conflict)
            408 -> Result.Error(NetworkError.RequestTimeout)
            in 500..599 -> Result.Error(NetworkError.ServerError)
            else -> Result.Error(NetworkError.Unknown)
        }
    }

    suspend fun loginUser(user: LoginUserDto): Result<LoginUserResponseDto, NetworkError> {
        val result = try {
            httpClient.post(LOGIN_ENDPOINT) {
                setBody(user)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NoInternet)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.Serialization)
        }

        return when (result.status.value) {
            in 200..299 -> {
                val user = result.body<LoginUserResponseDto>()
                Result.Success(user)
            }
//            401 -> Result.Error(NetworkError.Unathorized) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.Conflict)
            408 -> Result.Error(NetworkError.RequestTimeout)
            in 500..599 -> Result.Error(NetworkError.ServerError)
            else -> Result.Error(NetworkError.Unknown)
        }
    }
}