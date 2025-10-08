package com.discdogs.app.data.network.data.response.discogs.getSearch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Urls(
    @SerialName("last")
    val last: String? = null,
    @SerialName("next")
    val next: String? = null
)