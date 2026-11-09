package com.projach.comics_tracker.data.source.remote.mappers

import com.projach.comics_tracker.domain.dataResult.CommonDataError
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.dataResult.NetworkUserDataError
import com.projach.comics_tracker.domain.dataResult.ServerUserDataError
import okhttp3.Headers
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T : Any, E : DataError> safeApiCall(
    commonError: (CommonDataError) -> E,
    httpError: (code: Int, message: String, headers: Headers) -> E? = { _, _, _ -> null },
    call: suspend () -> Response<T>
): DataResult<T, E> {
    val response = try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (e: UnknownHostException) {
        return DataResult.Failure(commonError(NetworkUserDataError.NoInternet))
    } catch (e: SocketTimeoutException) {
        return DataResult.Failure(commonError(NetworkUserDataError.Timeout))
    } catch (e: IOException) {
        return DataResult.Failure(commonError(NetworkUserDataError.NoInternet))
    }

    val body = response.body()
    return when {
        response.isSuccessful && body != null -> DataResult.Success(body)
        response.isSuccessful -> DataResult.Failure(commonError(ServerUserDataError.Unknown(response.code())))
        else -> DataResult.Failure(
            httpError(response.code(), response.message(), response.headers())
                ?: commonError(response.code().toServerError())
        )
    }
}

private fun Int.toServerError(): ServerUserDataError = when (this) {
    503 -> ServerUserDataError.Unavailable
    in 500..599 -> ServerUserDataError.Internal
    else -> ServerUserDataError.Unknown(this)
}