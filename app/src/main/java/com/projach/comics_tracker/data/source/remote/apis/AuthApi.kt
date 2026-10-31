package com.projach.comics_tracker.data.source.remote.apis

import com.projach.comics_tracker.data.model.SignUpRequestDto
import com.projach.comics_tracker.domain.REGISTER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(REGISTER)
    suspend fun registerUser(
        @Body userDto: SignUpRequestDto
    ): Response<Unit>
}