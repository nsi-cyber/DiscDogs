package com.discdogs.app.data.network.data.response.discogs.getMasterDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,

    )