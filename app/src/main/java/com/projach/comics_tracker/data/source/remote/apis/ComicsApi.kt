package com.projach.comics_tracker.data.source.remote.apis

import com.projach.comics_tracker.data.model.ComicResponseDto
import com.projach.comics_tracker.domain.COMICS
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComicsApi {
    @GET(COMICS)
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<List<ComicResponseDto>>

    @DELETE("$COMICS/{id}")
    suspend fun deleteComic(
        @Path("id") id: Int
    ): Response<Unit>
}