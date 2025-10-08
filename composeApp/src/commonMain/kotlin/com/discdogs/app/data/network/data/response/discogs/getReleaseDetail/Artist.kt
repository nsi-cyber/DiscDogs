package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("anv")
    val anv: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("join")
    val join: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("role")
    val role: String? = null,
    @SerialName("tracks")
    val tracks: String? = null
)