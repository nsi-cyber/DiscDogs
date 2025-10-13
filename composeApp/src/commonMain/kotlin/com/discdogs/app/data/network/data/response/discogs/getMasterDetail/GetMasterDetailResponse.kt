package com.discdogs.app.data.network.data.response.discogs.getMasterDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMasterDetailResponse(
    @SerialName("artists")
    val artists: List<Artist?>? = null,
    @SerialName("data_quality")
    val dataQuality: String? = null,
    @SerialName("genres")
    val genres: List<String?>? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("images")
    val images: List<Image?>? = null,
    @SerialName("lowest_price")
    val lowestPrice: Double? = null,
    @SerialName("main_release")
    val mainRelease: Int? = null,
    @SerialName("main_release_url")
    val mainReleaseUrl: String? = null,
    @SerialName("num_for_sale")
    val numForSale: Int? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("styles")
    val styles: List<String?>? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("tracklist")
    val tracklist: List<Tracklist?>? = null,
    @SerialName("uri")
    val uri: String? = null,
    @SerialName("versions_url")
    val versionsUrl: String? = null,
    @SerialName("videos")
    val videos: List<Video?>? = null,
    @SerialName("year")
    val year: Int? = null
)