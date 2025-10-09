package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetReleaseDetailResponse(
    @SerialName("artists")
    val artists: List<Artist?>? = null,
    @SerialName("community")
    val community: Community? = null,
    @SerialName("companies")
    val companies: List<Company?>? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("data_quality")
    val dataQuality: String? = null,
    @SerialName("date_added")
    val dateAdded: String? = null,
    @SerialName("date_changed")
    val dateChanged: String? = null,
    @SerialName("estimated_weight")
    val estimatedWeight: Int? = null,
    @SerialName("extraartists")
    val extraArtists: List<ExtraArtist?>? = null,
    @SerialName("format_quantity")
    val formatQuantity: Int? = null,
    @SerialName("formats")
    val formats: List<Format?>? = null,
    @SerialName("genres")
    val genres: List<String?>? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("identifiers")
    val identifiers: List<Identifier?>? = null,
    @SerialName("images")
    val images: List<İmage?>? = null,
    @SerialName("labels")
    val labels: List<Label?>? = null,
    @SerialName("lowest_price")
    val lowestPrice: Double? = null,
    @SerialName("master_url")
    val masterUrl: String? = null,
    @SerialName("master_id")
    val masterId: Int? = null,
    @SerialName("notes")
    val notes: String? = null,
    @SerialName("num_for_sale")
    val numForSale: Int? = null,
    @SerialName("released")
    val released: String? = null,
    @SerialName("released_formatted")
    val releasedFormatted: String? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("styles")
    val styles: List<String?>? = null,
    @SerialName("thumb")
    val thumb: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("tracklist")
    val trackList: List<Tracklist?>? = null,
    @SerialName("uri")
    val uri: String? = null,
    @SerialName("videos")
    val videos: List<Video?>? = null,
    @SerialName("year")
    val year: Int? = null
)