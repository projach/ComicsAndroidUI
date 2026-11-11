package com.projach.comics_tracker.ui.screens.comicsList.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.ui.screens.comicsList.Content
import kotlinx.coroutines.flow.flowOf

@Preview(showBackground = true)
@Composable
fun ComicsScreenPreview() {
    val comics = flowOf(PagingData.from(previewComics)).collectAsLazyPagingItems()
    Content(
        comics = comics,
        isLoading = false,
        onDeleteComic = {},
        onComicClick = {},
        onLogoutClick = {},
        onAddNewComic = {}
    )
}

private val previewComics = List(1) { index ->
    ComicModel(
        id = index,
        issue = "$index",
        name = "Batman",
        publisher = "Tod",
        writer = "Tod",
        releaseDate = "May 2, 2025",
        acquiredDate = "Aug 2, 2025",
        cover = "B",
        comicShop = "Star Comics",
        price = "$2.30",
    )
}