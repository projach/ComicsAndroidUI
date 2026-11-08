package com.projach.comics_tracker.domain.dataResult

sealed interface DataError
sealed interface RegisterUserDataError : DataError
sealed interface VerifyOtpDataError : DataError
sealed interface RefreshOtpCodeDataError : DataError
sealed interface CommonDataUserDataError : RegisterUserDataError, VerifyOtpDataError,
    RefreshOtpCodeDataError