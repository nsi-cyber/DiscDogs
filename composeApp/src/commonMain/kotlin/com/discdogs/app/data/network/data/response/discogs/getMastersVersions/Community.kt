package com.discdogs.app.data.network.data.response.discogs.getMastersVersions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Community(
    @SerialName("in_collection")
    val inCollection: Int? = null,
    @SerialName("in_wantlist")
    val inWantlist: Int? = null
)