package com.discdogs.app.data.network.data.response.discogs.getMastersVersions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMastersVersionsResponse(
    @SerialName("catno")
    val catNo: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("label")
    val label: String? = null,
    @SerialName("major_formats")
    val majorFormats: List<String>? = null,
    @SerialName("released")
    val released: String? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("stats")
    val stats: Stats? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("thumb")
    val thumb: String? = null,
    @SerialName("title")
    val title: String? = null
)