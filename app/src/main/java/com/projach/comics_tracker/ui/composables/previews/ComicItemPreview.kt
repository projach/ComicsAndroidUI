package com.projach.comics_tracker.ui.composables.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.ui.composables.ComicItem

@Preview(showBackground = true, heightDp = 100)
@Composable
fun ComicItemPreview() {
    ComicItem(
        ComicModel(
            id = 1,
            issue = "#2",
            name = "Batman",
            publisher = "DC",
            writer = "Ricky d",
            releaseDate = "May 3, 2026",
            acquiredDate = "Aug 5, 2026",
            cover = "Cover B",
            comicShop = "Star Comics",
            price = "$ 4.14"
        ),
        onDeleteComic = {},
        onComicClick = {}
    )
}