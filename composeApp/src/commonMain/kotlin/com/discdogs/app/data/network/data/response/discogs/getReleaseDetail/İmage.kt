package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class İmage(
    @SerialName("height")
    val height: Int? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("uri")
    val uri: String? = null,
    @SerialName("uri150")
    val uri150: String? = null,
    @SerialName("width")
    val width: Int? = null
)