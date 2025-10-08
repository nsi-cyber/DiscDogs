package com.discdogs.app.data.network.data.response.discogs.getSearch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDiscogsSearchResponse(
    @SerialName("barcode")
    val barcode: List<String?>? = null,
    @SerialName("catno")
    val catNo: String? = null,
    @SerialName("community")
    val community: Community? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("format")
    val format: List<String?>? = null,
    @SerialName("genre")
    val genre: List<String?>? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("label")
    val label: List<String?>? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("style")
    val style: List<String?>? = null,
    @SerialName("thumb")
    val thumb: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("uri")
    val uri: String? = null,
    @SerialName("year")
    val year: String? = null
)