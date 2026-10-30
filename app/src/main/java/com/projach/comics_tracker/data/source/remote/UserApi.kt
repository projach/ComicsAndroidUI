package com.projach.comics_tracker.data.source.remote

import com.projach.comics_tracker.data.model.UserDto
import com.projach.comics_tracker.domain.REGISTER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST(REGISTER)
    suspend fun registerUser(
        @Body userDto: UserDto
    ): Response<Unit>
}