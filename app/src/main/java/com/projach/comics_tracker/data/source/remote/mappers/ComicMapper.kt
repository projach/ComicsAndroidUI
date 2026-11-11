package com.projach.comics_tracker.data.source.remote.mappers

import com.projach.comics_tracker.data.model.ComicResponseDto
import com.projach.comics_tracker.domain.models.ComicModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)

fun ComicResponseDto.toModel() = ComicModel(
    id = id,
    issue = "#$issue",
    name = name,
    publisher = publisher,
    writer = writer,
    releaseDate = releaseDate?.let { LocalDate.parse(it).toDisplay() },
    acquiredDate = acquiredDate?.let { LocalDate.parse(it).toDisplay() },
    cover = cover,
    comicShop = comicShop,
    price = "$$price"
)

fun LocalDate.toDisplay(): String = format(displayFormatter)