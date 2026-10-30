package com.projach.comics_tracker.data.source.remote

import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import retrofit2.Response

fun Response<*>.toDataResult(): DataResult<Unit, DataError> = when {
    isSuccessful -> DataResult.Success(Unit)
    code() == 409 -> DataResult.Failure(DataError.Conflict(message()))
    else -> DataResult.Failure(DataError.Unknown)
}