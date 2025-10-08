package com.discdogs.app.data.network.data.response.deezer.search



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(

    @SerialName("preview")
    val preview: String,
//
//    @SerialName("album")
//    val album: Album,
//    @SerialName("artist")
//    val artist: Artist,
//    @SerialName("duration")
//    val duration: String,
//    @SerialName("explicit_content_cover")
//    val explicitContentCover: Int,
//    @SerialName("explicit_content_lyrics")
//    val explicitContentLyrics: Int,
//    @SerialName("explicit_lyrics")
//    val explicitLyrics: Boolean,
//    @SerialName("id")
//    val id: String,
//    @SerialName("link")
//    val link: String,
//    @SerialName("md5_image")
//    val md5Image: String,
//    @SerialName("rank")
//    val rank: String,
//    @SerialName("readable")
//    val readable: Boolean,
//    @SerialName("title")
//    val title: String,
//    @SerialName("title_short")
//    val titleShort: String,
//    @SerialName("title_version")
//    val titleVersion: String,
//    @SerialName("type")
//    val type: String
)