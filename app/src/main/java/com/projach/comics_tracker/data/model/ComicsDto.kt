package com.projach.comics_tracker.data.model

import com.google.gson.annotations.SerializedName
import com.projach.comics_tracker.domain.models.ComicModel
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ComicResponseDto(
    val id: String,
    val issue: String,
    val name: String,
    val publisher: String?,
    val writer: String,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("aquired_date") val acquiredDate: String?,
    val cover: String?,
    @SerializedName("comic_shop") val comicShop: String?,
    val price: Float?
) {
    fun toModel(): ComicModel = ComicModel(
        id = id,
        issue = issue,
        name = name,
        publisher = publisher,
        writer = writer,
        releaseDate = releaseDate?.let { LocalDate.parse(it) },
        acquiredDate = acquiredDate?.let { LocalDate.parse(it) },
        cover = cover,
        comicShop = comicShop,
        price = price
    )
}