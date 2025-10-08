package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Format(
    @SerialName("descriptions")
    val descriptions: List<String?>? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("qty")
    val qty: String? = null
)