package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Submitter(
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("username")
    val username: String? = null
)