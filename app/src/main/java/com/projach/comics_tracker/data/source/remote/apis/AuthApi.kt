package com.projach.comics_tracker.data.source.remote.apis

import com.projach.comics_tracker.data.model.LogInRequestDto
import com.projach.comics_tracker.data.model.LoginResponseDto
import com.projach.comics_tracker.data.model.RefreshOtpResponseDto
import com.projach.comics_tracker.data.model.ResendOtpRequestDto
import com.projach.comics_tracker.data.model.SignUpRequestDto
import com.projach.comics_tracker.data.model.UserDto
import com.projach.comics_tracker.data.model.VerifyRequestDto
import com.projach.comics_tracker.domain.LOGIN
import com.projach.comics_tracker.domain.REGISTER
import com.projach.comics_tracker.domain.RESEND_OTP
import com.projach.comics_tracker.domain.VERIFY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(REGISTER)
    suspend fun registerUser(
        @Body userDto: SignUpRequestDto
    ): Response<Unit>

    @POST(VERIFY)
    suspend fun verifyUser(
        @Body verifyDto: VerifyRequestDto
    ): Response<UserDto>

    @POST(RESEND_OTP)
    suspend fun resendOtp(
        @Body resendCodeDto: ResendOtpRequestDto
    ): Response<RefreshOtpResponseDto>

    @POST(LOGIN)
    suspend fun loginUser(
        @Body logInRequestDto: LogInRequestDto
    ): Response<LoginResponseDto>
}