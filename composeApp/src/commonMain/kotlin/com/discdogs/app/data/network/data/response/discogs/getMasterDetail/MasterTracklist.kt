package com.discdogs.app.data.network.data.response.discogs.getMasterDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MasterTracklist(
    @SerialName("duration")
    val duration: String?,
    @SerialName("position")
    val position: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("type_")
    val type_: String?
)