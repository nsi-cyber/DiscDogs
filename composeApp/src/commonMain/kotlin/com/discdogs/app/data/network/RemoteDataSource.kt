package com.discdogs.app.data.network

import com.discdogs.app.core.data.ResultWrapper
import com.discdogs.app.data.network.data.response.base.PaginationBaseResponse
import com.discdogs.app.data.network.data.response.deezer.search.GetSearchResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.data.network.data.response.gemini.GeminiBaseResponse

interface RemoteDataSource {

    suspend fun searchBarcode(
        barcode: String?,
        perPage: Int = 1,
        page: Int = 1,
    ): ResultWrapper<PaginationBaseResponse<GetDiscogsSearchResponse>>

    suspend fun getMastersVersions(
        masterId: Int,
        perPage: Int = 100,
        page: Int = 1,
    ): ResultWrapper<PaginationBaseResponse<GetMastersVersionsResponse>>

    suspend fun searchVinyl(
        query: String?,
        type: String = "release",
        perPage: Int = 100,
        page: Int = 1,
    ): ResultWrapper<PaginationBaseResponse<GetDiscogsSearchResponse>>

    suspend fun getReleaseDetail(
        releaseId: Int?,
    ): ResultWrapper<GetReleaseDetailResponse>


    suspend fun searchSongPreview(
        query: String?,
        limit: Int = 1,
    ): ResultWrapper<GetSearchResponse>

    suspend fun generateImageCaption(
        imageBytes: ByteArray,
        prompt: String = "Caption this image."
    ): ResultWrapper<GeminiBaseResponse>
}