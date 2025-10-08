package com.discdogs.app.presentation.model

 import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.Identifier
 import kotlin.uuid.ExperimentalUuidApi
 import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun GetReleaseDetailResponse?.toUiModel(): VinylDetailUiModel? {
    return this?.let { response ->
        VinylDetailUiModel(
            id = response.id,
            artists = response.artists?.map { ArtistUiModel(
                name = it?.name.orEmpty(),
                resourceUrl = it?.resourceUrl
            ) },
            //community = response.community,
            companies = response.companies?.map { CompanyUiModel(
                catNo = it?.catNo,
                entityType = it?.entityType,
                entityTypeName = it?.entityTypeName,
                id = it?.id,
                name = it?.name,
                resourceUrl = it?.resourceUrl
            ) },
            country = response.country,
            barcode = identifiers?.firstOrNull { it?.type == "Barcode" }?.value?.replace(
                "\\s".toRegex(),
                ""
            )?.replace("-".toRegex(), ""),
            extraArtists = response.extraArtists?.map {  ArtistUiModel(
                name = it?.name.orEmpty(),
                resourceUrl = it?.resourceUrl
            ) },
            formatQuantity = response.formatQuantity,
            formats = response.formats?.map { FormatUiModel(
                descriptions = it?.descriptions?.map { it.orEmpty() },
                name = it?.name.orEmpty(),
                qty = it?.qty
            ) },
            genres = response.genres?.map { it.orEmpty() },
            identifiers = response.identifiers,
            images = response.images?.map { ImageUiModel(
                height = it?.height,
                resourceUrl = it?.resourceUrl,
                uri = it?.uri,
                width = it?.width
            ) },
            labels = response.labels?.map { LabelUiModel(
                catNo = it?.catNo,
                entityType = it?.entityType,
                id = it?.id ?: 0,
                name = it?.name,
                resourceUrl = it?.resourceUrl
            ) },
            masterId = response.masterId,
            notes = response.notes,
            released = response.released,
            releasedFormatted = response.releasedFormatted,
            resourceUrl = response.resourceUrl,
            status = response.status,
            styles = response.styles?.map { it.orEmpty() },
            thumb = response.thumb,
            title = response.title,
            trackList = response.trackList?.filter { it?.type?.contains("track")==true }?.map { TrackListUiModel(
                id = Uuid.random().toString(),
                duration = it?.duration,
                position = it?.position,
                title = it?.title.orEmpty(),
                type = it?.type.orEmpty()
            ) },
            uri = response.uri,
            year = response.year
        )
    }
}


data class VinylDetailUiModel(
    val id: Int,
    val artists: List<ArtistUiModel>? = null,
    //val community: Community? = null,
    val companies: List<CompanyUiModel>? = null,
    val country: String? = null,
    val barcode: String? = null,


    val extraArtists: List<ArtistUiModel>? = null,
    val formatQuantity: Int? = null,

    val formats: List<FormatUiModel>? = null,
    val genres: List<String>? = null,

    val identifiers: List<Identifier?>? = null,
    val images: List<ImageUiModel>? = null,
    val labels: List<LabelUiModel>? = null,


    val masterId: Int,
    val notes: String? = null,

    val released: String? = null,
    val releasedFormatted: String? = null,
    val resourceUrl: String? = null,

    val status: String? = null,
    val styles: List<String>? = null,

    val thumb: String? = null,

    val title: String,
    val trackList: List<TrackListUiModel>? = null,
    val uri: String? = null,
    val year: Int? = null
)

data class ArtistUiModel(
    val name: String,
    val resourceUrl: String? = null,
)

data class TrackListUiModel(
    val id: String,
    val duration: String? = null,
    val position: String? = null,
    val title: String,
    val type: String
)


data class FormatUiModel(
    val descriptions: List<String>? = null,
    val name: String,
    val qty: String? = null
)

data class ImageUiModel(
    val height: Int? = null,
    val resourceUrl: String? = null,
    val uri: String? = null,
    val width: Int? = null
)

data class LabelUiModel(
    val catNo: String? = null,
    val entityType: String? = null,
    val id: Int,
    val name: String? = null,
    val resourceUrl: String? = null
)


data class CompanyUiModel(
    val catNo: String? = null,
    val entityType: String? = null,
    val entityTypeName: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val resourceUrl: String? = null
)