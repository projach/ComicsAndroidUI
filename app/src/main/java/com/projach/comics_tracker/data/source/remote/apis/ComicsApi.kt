package com.projach.comics_tracker.data.source.remote.apis

import com.projach.comics_tracker.data.model.ComicResponseDto
import com.projach.comics_tracker.domain.COMICS
import retrofit2.Response
import retrofit2.http.GET

interface ComicsApi {
    @GET(COMICS)
    suspend fun getComics(): Response<List<ComicResponseDto>>
}