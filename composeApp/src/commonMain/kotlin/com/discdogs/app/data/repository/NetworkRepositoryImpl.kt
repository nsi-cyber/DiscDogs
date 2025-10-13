package com.discdogs.app.data.repository

import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.data.toResource
import com.discdogs.app.data.network.RemoteDataSource
import com.discdogs.app.data.network.data.response.base.PaginationBaseResponse
import com.discdogs.app.data.network.data.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.data.network.data.response.gemini.GeminiResponse
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.domain.SearchType
import kotlinx.serialization.json.Json


class NetworkRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : NetworkRepository {

    override suspend fun searchBarcode(
        barcode: String?,
    ): Resource<Int?> =
        remoteDataSource
            .searchBarcode(barcode)
            .toResource { it.results?.firstOrNull()?.id }

    override suspend fun searchVinyl(
        query: String?,
        type: SearchType,
        perPage: Int,
        page: Int
    ): Resource<PaginationBaseResponse<GetDiscogsSearchResponse>?> =
        remoteDataSource
            .searchVinyl(
                query = query,
                type = type,
                perPage = perPage,
                page = page
            )
            .toResource { it }

    override suspend fun getMastersVersions(
        masterId: Int,
        perPage: Int,
        page: Int
    ): Resource<PaginationBaseResponse<GetMastersVersionsResponse>?> =
        remoteDataSource
            .getMastersVersions(
                masterId = masterId,
                perPage = perPage,
                page = page
            )
            .toResource { it }

    override suspend fun getReleaseDetail(releaseId: Int?): Resource<GetReleaseDetailResponse?> =
        remoteDataSource
            .getReleaseDetail(releaseId = releaseId)
            .toResource { it }

    override suspend fun getMasterDetail(masterId: Int?): Resource<GetMasterDetailResponse?> =
        remoteDataSource
            .getMasterDetail(masterId = masterId)
            .toResource { it }

    override suspend fun searchSongPreview(
        query: String?,
    ): Resource<String?> =
        remoteDataSource
            .searchSongPreview(
                query = query,
            ).toResource { it.searchResponseList.firstOrNull()?.preview }

    override suspend fun generateImageCaption(
        imageBytes: ByteArray,
    ): Resource<GeminiResponse?> =
        remoteDataSource
            .generateImageCaption(
                imageBytes = imageBytes,
                prompt = "If image has a vinyl record return {\"name\":\"<full record name>\",\"response\":200}, else return {\"name\":null,\"response\":404}"
            )
            .toResource {
                Json.decodeFromString<GeminiResponse>(
                    it.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace(
                        "```json",
                        ""
                    )?.replace("```", "").orEmpty()
                )
            }

}