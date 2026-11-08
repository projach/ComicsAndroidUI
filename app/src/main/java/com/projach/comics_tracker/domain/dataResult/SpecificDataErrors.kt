package com.projach.comics_tracker.domain.dataResult

data class Conflict(val message: String) : RegisterUserDataError, VerifyOtpDataError
data class TooManyRequests(val retryAfterRemainingSeconds: Int?) : RegisterUserDataError,
    RefreshOtpCodeDataError

object InvalidOrExpiredCode : VerifyOtpDataError
object NotFound : RefreshOtpCodeDataError
object InvalidUsernameOrPassword : LoginUserDataError