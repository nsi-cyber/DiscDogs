package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Community(
    @SerialName("contributors")
    val contributors: List<Contributor?>? = null,
    @SerialName("data_quality")
    val dataQuality: String? = null,
    @SerialName("have")
    val have: Int? = null,
    @SerialName("rating")
    val rating: Rating? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("submitter")
    val submitter: Submitter? = null,
    @SerialName("want")
    val want: Int? = null
)