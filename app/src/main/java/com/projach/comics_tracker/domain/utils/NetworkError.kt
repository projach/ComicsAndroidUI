package com.projach.comics_tracker.domain.utils

sealed interface NetworkError : Error {
    object Unauthorized : NetworkError
    object Conflict : NetworkError
    object OtpTimeout : NetworkError
    object NoInternet : NetworkError
    object Serialization : NetworkError
    object RequestTimeout : NetworkError
    object ServerError : NetworkError
    object Unknown : NetworkError;
}