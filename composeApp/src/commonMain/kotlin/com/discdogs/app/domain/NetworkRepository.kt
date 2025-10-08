package com.discdogs.app.domain

import com.discdogs.app.core.data.Resource
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse


interface NetworkRepository {

    suspend fun searchBarcode(
        barcode: String?,
    ): Resource<Int?>

    suspend fun searchVinyl(
        query: String?
    ): Resource<List<GetDiscogsSearchResponse>?>

    suspend fun getMastersVersions(
        masterId: Int,
    ): Resource<List<GetMastersVersionsResponse>?>

    suspend fun getReleaseDetail(releaseId: Int?): Resource<GetReleaseDetailResponse?>

    suspend fun searchSongPreview(
        query: String?,
    ): Resource<String?>

}