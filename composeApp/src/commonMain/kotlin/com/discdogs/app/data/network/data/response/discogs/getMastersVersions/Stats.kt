package com.discdogs.app.data.network.data.response.discogs.getMastersVersions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    @SerialName("community")
    val community: Community? = null,
    @SerialName("user")
    val user: User? = null
)