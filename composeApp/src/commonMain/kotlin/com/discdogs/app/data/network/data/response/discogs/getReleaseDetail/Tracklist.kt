package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tracklist(
    @SerialName("duration")
    val duration: String? = null,
    @SerialName("position")
    val position: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("type_")
    val type: String? = null
)