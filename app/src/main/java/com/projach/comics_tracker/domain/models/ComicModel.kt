package com.projach.comics_tracker.domain.models

import java.time.LocalDate

data class ComicModel(
    val id: String,
    val issue: String,
    val name: String,
    val publisher: String?,
    val writer: String,
    val releaseDate: LocalDate?,
    val acquiredDate: LocalDate?,
    val cover: String?,
    val comicShop: String?,
    val price: Float?
)
