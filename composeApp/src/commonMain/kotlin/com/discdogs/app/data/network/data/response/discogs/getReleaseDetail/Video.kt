package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerialName("description")
    val description: String? = null,
    @SerialName("duration")
    val duration: Int? = null,
    @SerialName("embed")
    val embed: Boolean? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("uri")
    val uri: String? = null
)