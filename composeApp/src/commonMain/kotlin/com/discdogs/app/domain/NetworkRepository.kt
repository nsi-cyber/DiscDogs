package com.discdogs.app.domain

import com.discdogs.app.core.data.Resource
import com.discdogs.app.data.network.data.response.base.PaginationBaseResponse
import com.discdogs.app.data.network.data.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.data.network.data.response.gemini.GeminiResponse


interface NetworkRepository {

    suspend fun searchBarcode(
        barcode: String?,
    ): Resource<Int?>

    suspend fun searchVinyl(
        query: String?,
        type: SearchType,
        perPage: Int = 20,
        page: Int = 1
    ): Resource<PaginationBaseResponse<GetDiscogsSearchResponse>?>

    suspend fun getMastersVersions(
        masterId: Int,
        perPage: Int = 20,
        page: Int = 1
    ): Resource<PaginationBaseResponse<GetMastersVersionsResponse>?>

    suspend fun getReleaseDetail(releaseId: Int?): Resource<GetReleaseDetailResponse?>
    suspend fun getMasterDetail(releaseId: Int?): Resource<GetMasterDetailResponse?>

    suspend fun searchSongPreview(
        query: String?,
    ): Resource<String?>

    suspend fun generateImageCaption(
        imageBytes: ByteArray,
    ): Resource<GeminiResponse?>

}