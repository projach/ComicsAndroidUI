package com.projach.comics_tracker.domain.useCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projach.comics_tracker.data.source.remote.paging.ComicsPagingSource
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicsUseCase @Inject constructor(
    private val repository: ComicsRepository
) {
    fun getComics(): Flow<PagingData<ComicModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ComicsPagingSource(repository) }
        ).flow
    }

    suspend fun deleteComic(id: Int): DataResult<Unit, DataError> = repository.deleteComic(id)
}