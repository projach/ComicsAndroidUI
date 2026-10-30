package com.projach.comics_tracker.domain.dataResult

sealed interface DataResult<out T, out E> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>
    data class Failure<out E : DataError>(val error: E) : DataResult<Nothing, E>
}

inline fun <T, E : DataError> DataResult<T, E>.onSuccess(block: (T) -> Unit): DataResult<T, E> {
    if (this is DataResult.Success) block(data)
    return this
}

inline fun <T, E : DataError> DataResult<T, E>.onError(block: (E) -> Unit): DataResult<T, E> {
    if (this is DataResult.Failure) block(error)
    return this
}

inline fun <T, E : DataError, R> DataResult<T, E>.map(transform: (T) -> R): DataResult<R, E> =
    when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Failure -> this
    }

inline fun <T, E : DataError, R> DataResult<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R,
): R = when (this) {
    is DataResult.Success -> onSuccess(data)
    is DataResult.Failure -> onFailure(error)
}