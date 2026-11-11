package com.projach.comics_tracker.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ComicResponseDto(
    val id: Int,
    val issue: Int,
    val name: String,
    val publisher: String?,
    val writer: String,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("aquired_date") val acquiredDate: String?,
    val cover: String?,
    @SerializedName("comic_shop") val comicShop: String?,
    val price: Float?
)