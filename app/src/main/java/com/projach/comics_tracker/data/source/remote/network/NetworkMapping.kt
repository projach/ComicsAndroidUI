package com.projach.comics_tracker.data.source.remote.network

import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import retrofit2.Response

fun Response<*>.toDataResult(): DataResult<Unit, DataError> = when {
    isSuccessful -> DataResult.Success(Unit)
    code() == 409 -> DataResult.Failure(DataError.Conflict(message()))
    code() == 429 -> DataResult.Failure(DataError.TooManyRequests(headers()["Retry-After"]?.toIntOrNull()))
    else -> DataResult.Failure(DataError.Unknown())
}