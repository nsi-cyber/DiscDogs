package com.discdogs.app.presentation.model

import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.Stats


fun GetMastersVersionsResponse.toUiModel(): MastersVersionsUiModel {
    return MastersVersionsUiModel(
        catNo = catNo,
        country = country,
        format = format,
        id = id,
        label = label,
        majorFormats = majorFormats,
        released = released,
        stats = stats,
        status = status,
        thumb = thumb.orEmpty(),
        title = title.orEmpty()
    )
}


data class MastersVersionsUiModel(
    val catNo: String? = null,
    val country: String? = null,
    val format: String? = null,
    val id: Int,
    val label: String? = null,
    val majorFormats: List<String>? = null,
    val released: String? = null,
    val stats: Stats? = null,
    val status: String? = null,
    val thumb: String,
    val title: String
)