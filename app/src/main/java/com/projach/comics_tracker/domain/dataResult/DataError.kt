package com.projach.comics_tracker.domain.dataResult

sealed interface DataError {
    data object Unknown : DataError
    data class Conflict(val message: String) : DataError
}