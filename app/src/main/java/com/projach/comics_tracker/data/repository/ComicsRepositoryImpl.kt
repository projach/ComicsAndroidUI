package com.projach.comics_tracker.data.repository

import com.projach.comics_tracker.data.source.remote.apis.ComicsApi
import com.projach.comics_tracker.data.source.remote.mappers.safeApiCall
import com.projach.comics_tracker.data.source.remote.mappers.toModel
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.dataResult.map
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.domain.repository.ComicsRepository
import javax.inject.Inject

class ComicsRepositoryImpl @Inject constructor(
    val comicsApi: ComicsApi
) : ComicsRepository {
    override suspend fun getComics(
        offset: Int,
        limit: Int
    ): DataResult<List<ComicModel>, DataError> =
        safeApiCall(
            commonError = { it },
            call = { comicsApi.getComics(offset, limit) }
        ).map {
            it.map { comic ->
                comic.toModel()
            }
        }

    override suspend fun deleteComic(id: Int): DataResult<Unit, DataError> =
        safeApiCall(
            commonError = { it },
            call = { comicsApi.deleteComic(id) }
        )

}