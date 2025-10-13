package com.discdogs.app.presentation.model

import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse


fun GetDiscogsSearchResponse.toUiModel(isMaster: Boolean): VinylResultUiModel {
    return VinylResultUiModel(
        id = id,
        thumb = thumb.orEmpty(),
        format = if (isMaster) null else format?.map { it.orEmpty() },
        title = title.orEmpty(),
        year = year.orEmpty(),
        genre = genre?.map { it.orEmpty() }
    )
}


data class VinylResultUiModel(
    val id: Int,
    val thumb: String,
    val title: String,
    val format: List<String>? = null,
    val year: String,
    val genre: List<String>? = null,
)
