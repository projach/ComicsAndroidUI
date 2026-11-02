package com.projach.comics_tracker.domain.dataResult

sealed interface DataError {
    data class Unknown(val cause: String = "") : DataError
    data class Conflict(val message: String) : DataError
    data class TooManyRequests(val retryAfterRemainingSeconds: Int?) : DataError
    object Timeout : DataError
    object NoInternet : DataError
    object Network : DataError
}