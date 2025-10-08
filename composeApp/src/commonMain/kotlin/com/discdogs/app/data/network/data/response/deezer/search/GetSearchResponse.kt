package com.discdogs.app.data.network.data.response.deezer.search



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchResponse(
    @SerialName("data")
    val searchResponseList: List<SearchResponse>,
    @SerialName("next")
    val next: String,
    @SerialName("total")
    val total: Int
)

