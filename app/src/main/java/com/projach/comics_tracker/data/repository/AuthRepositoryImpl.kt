package com.projach.comics_tracker.data.repository

import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.data.model.SignUpRequestDto
import com.projach.comics_tracker.data.source.remote.apis.AuthApi
import com.projach.comics_tracker.data.source.remote.network.toDataResult
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.repository.AuthRepository
import kotlinx.serialization.SerializationException
import okio.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): DataResult<Unit, DataError> =
        try {
            authApi.registerUser(
                SignUpRequestDto(
                    username = username,
                    password = password,
                    email = email
                )
            ).toDataResult()
        } catch (e: SocketTimeoutException) {
            Logger.e("UsersRepositoryImpl", "error $e")
            DataResult.Failure(DataError.Timeout)
        } catch (e: UnknownHostException) {
            Logger.e("UsersRepositoryImpl", "error $e")
            DataResult.Failure(DataError.NoInternet)
        } catch (e: ConnectException) {
            Logger.e("UsersRepositoryImpl", "error $e")
            DataResult.Failure(DataError.NoInternet)
        } catch (e: IOException) {
            Logger.e("UsersRepositoryImpl", "error $e")
            DataResult.Failure(DataError.Network)
        } catch (e: SerializationException) {
            Logger.e("UsersRepositoryImpl", "error $e")
            DataResult.Failure(DataError.Unknown(e.message ?: ""))
        }
}