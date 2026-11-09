package com.projach.comics_tracker.domain.repository

import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.models.ComicModel

interface ComicsRepository {
    suspend fun getComics(): DataResult<List<ComicModel>, DataError>
}