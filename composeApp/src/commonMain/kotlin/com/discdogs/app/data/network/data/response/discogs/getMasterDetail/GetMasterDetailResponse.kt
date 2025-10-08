package com.discdogs.app.data.network.data.response.discogs.getMasterDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMasterDetailResponse(
    @SerialName("artists")
    val artists: List<Artist?>?,
    @SerialName("genres")
    val genres: List<String?>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("main_release")
    val main_release: Int?,
    @SerialName("styles")
    val styles: List<String?>?,
    @SerialName("title")
    val title: String?,
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("tracklist")
    val masterTracklist: List<MasterTracklist?>?,
    @SerialName("year")
    val year: Int?
)