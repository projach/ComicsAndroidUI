package com.projach.comics_tracker.data.source.remote

import com.projach.comics_tracker.data.models.users.LoginUserDto
import com.projach.comics_tracker.data.models.users.LoginUserResponseDto
import com.projach.comics_tracker.data.models.users.RegisterUserDto
import com.projach.comics_tracker.data.models.users.RegisterUserResponseDto
import com.projach.comics_tracker.domain.LOGIN_ENDPOINT
import com.projach.comics_tracker.domain.REGISTER_ENDPOINT
import com.projach.comics_tracker.domain.utils.NetworkError
import com.projach.comics_tracker.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class UserClient(
    private val httpClient: HttpClient
) {
    suspend fun registerUser(user: RegisterUserDto): Result<String, NetworkError> {
        val result = try {
            httpClient.post(REGISTER_ENDPOINT) {
                setBody(user)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when (result.status.value) {
            in 200..299 -> {
                val user = result.body<RegisterUserResponseDto>()
                Result.Success(user.username)
            }
//            401 -> Result.Error(NetworkError.UNAUTHORIZED) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun loginUser(user: LoginUserDto): Result<LoginUserResponseDto, NetworkError> {
        val result = try {
            httpClient.post(LOGIN_ENDPOINT) {
                setBody(user)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when (result.status.value) {
            in 200..299 -> {
                val user = result.body<LoginUserResponseDto>()
                Result.Success(user)
            }
//            401 -> Result.Error(NetworkError.UNAUTHORIZED) TODO(handle this response centrally)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}