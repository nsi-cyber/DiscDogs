package com.discdogs.app.data.network.data.response.discogs.getSearch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("items")
    val items: Int? = null,
    @SerialName("page")
    val page: Int,
    @SerialName("pages")
    val pages: Int? = null,
    @SerialName("per_page")
    val perPage: Int? = null,
    @SerialName("urls")
    val urls: Urls? = null
)