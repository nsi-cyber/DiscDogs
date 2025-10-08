package com.discdogs.app.data.repository

import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.data.toResource
import com.discdogs.app.data.network.RemoteDataSource
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.domain.NetworkRepository


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
        query: String?
    ): Resource<List<GetDiscogsSearchResponse>?> =
        remoteDataSource
            .searchVinyl(query = query)
            .toResource { it.results }

    override suspend fun getMastersVersions(
        masterId: Int,
    ): Resource<List<GetMastersVersionsResponse>?> =
        remoteDataSource
            .getMastersVersions(masterId = masterId)
            .toResource { it.results }

    override suspend fun getReleaseDetail(releaseId: Int?): Resource<GetReleaseDetailResponse?> =
        remoteDataSource
            .getReleaseDetail(releaseId = releaseId)
            .toResource { it }

    override suspend fun searchSongPreview(
        query: String?,
    ): Resource<String?> =
        remoteDataSource
            .searchSongPreview(
                query = query,
            ).toResource { it.searchResponseList.firstOrNull()?.preview }

}