package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    @SerialName("average")
    val average: Double? = null,
    @SerialName("count")
    val count: Int? = null
)