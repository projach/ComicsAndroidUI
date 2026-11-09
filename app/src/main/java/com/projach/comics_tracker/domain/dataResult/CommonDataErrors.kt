package com.projach.comics_tracker.domain.dataResult

sealed interface NetworkUserDataError : CommonDataError {
    object Timeout : NetworkUserDataError
    object NoInternet : NetworkUserDataError
}

sealed interface ServerUserDataError : CommonDataError {
    data object Internal : ServerUserDataError
    data object Unavailable : ServerUserDataError
    data class Unknown(val code: Int? = null) : ServerUserDataError
}