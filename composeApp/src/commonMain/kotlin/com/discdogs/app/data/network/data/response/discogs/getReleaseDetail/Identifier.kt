package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identifier(
    @SerialName("type")
    val type: String? = null,
    @SerialName("value")
    val value: String? = null
)