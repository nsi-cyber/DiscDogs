package com.discdogs.app.data.network.data.response.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationBaseResponse<T>(
    @SerialName("pagination")
    val pagination: Pagination? = null,
    @SerialName("results")
    val results: List<T>? = null,
    @SerialName("versions")
    val versions: List<T>? = null,
)

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

@Serializable
data class Urls(
    @SerialName("last")
    val last: String? = null,
    @SerialName("next")
    val next: String? = null
)
