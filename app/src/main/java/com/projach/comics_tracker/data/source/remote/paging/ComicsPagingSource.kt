package com.projach.comics_tracker.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.domain.repository.ComicsRepository

class ComicsPagingSource(
    private val repository: ComicsRepository
) : PagingSource<Int, ComicModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicModel> {
        val offset = params.key ?: 0
        val limit = params.loadSize.coerceAtMost(100)

        return when (val result = repository.getComics(offset, limit)) {
            is DataResult.Success -> LoadResult.Page(
                data = result.data,
                prevKey = null,
                nextKey = if (result.data.size < limit) null else offset + result.data.size
            )

            is DataResult.Failure -> LoadResult.Error(PagingException(result.error))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicModel>): Int? =
        state.anchorPosition?.let { maxOf(0, it - state.config.initialLoadSize / 2) }
}

class PagingException(val error: DataError) : Throwable()