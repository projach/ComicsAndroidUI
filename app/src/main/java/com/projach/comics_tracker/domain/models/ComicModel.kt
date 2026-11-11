package com.projach.comics_tracker.domain.models

data class ComicModel(
    val id: Int,
    val issue: String,
    val name: String,
    val publisher: String?,
    val writer: String,
    val releaseDate: String?,
    val acquiredDate: String?,
    val cover: String?,
    val comicShop: String?,
    val price: String?
)
