package com.discdogs.app.data.network.data.response.discogs.getSearch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Community(
    @SerialName("have")
    val have: Int? = null,
    @SerialName("want")
    val want: Int? = null
)