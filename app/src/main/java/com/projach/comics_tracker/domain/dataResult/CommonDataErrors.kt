package com.projach.comics_tracker.domain.dataResult

sealed interface NetworkUserDataError : CommonDataUserDataError {
    object Timeout : NetworkUserDataError
    object NoInternet : NetworkUserDataError
}

sealed interface ServerUserDataError : CommonDataUserDataError {
    data object Internal : ServerUserDataError
    data object Unavailable : ServerUserDataError
    data class Unknown(val code: Int? = null) : ServerUserDataError
}