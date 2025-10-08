package com.discdogs.app.data.network

import com.discdogs.app.core.data.ResultWrapper
import com.discdogs.app.core.data.safeApiCall
import com.discdogs.app.data.network.data.response.base.PaginationBaseResponse
import com.discdogs.app.data.network.data.response.deezer.search.GetSearchResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter



object Network {
    object Discogs {
        const val BASE_URL = "https://api.discogs.com"
        const val API_KEY = "YWyyVemvefOgexZXdCcDLlVzOgxBbHKPbzuzoywB"
        const val SEARCH = "/database/search"
    }

    object Deezer {
        const val BASE_URL = "https://api.deezer.com"
        const val SEARCH = "/search"
    }
}

class KtorRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteDataSource {

    override suspend fun searchBarcode(
        barcode: String?,
        perPage: Int,
        page: Int,
    ): ResultWrapper<PaginationBaseResponse<GetDiscogsSearchResponse>> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Discogs.BASE_URL}${Network.Discogs.SEARCH}"
            ) {
                parameter("barcode", barcode)
                parameter("per_page", perPage)
                parameter("page", page)
                parameter("token", Network.Discogs.API_KEY)
            }
        }
    }

    override suspend fun getMastersVersions(
        masterId: Int,
        perPage: Int,
        page: Int,
    ): ResultWrapper<PaginationBaseResponse<GetMastersVersionsResponse>> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Discogs.BASE_URL}/masters/${masterId}/versions"
            ) {
                parameter("master_id", masterId)
                parameter("per_page", perPage)
                parameter("page", page)
                parameter("token", Network.Discogs.API_KEY)
            }
        }
    }

    override suspend fun searchVinyl(
        query: String?,
        type: String,
        perPage: Int,
        page: Int
    ): ResultWrapper<PaginationBaseResponse<GetDiscogsSearchResponse>> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Discogs.BASE_URL}${Network.Discogs.SEARCH}"
            ) {
                parameter("query", query)
                parameter("type", type)
                parameter("per_page", perPage)
                parameter("page", page)
                parameter("token", Network.Discogs.API_KEY)
            }
        }
    }

    override suspend fun getReleaseDetail(releaseId: Int?): ResultWrapper<GetReleaseDetailResponse> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Discogs.BASE_URL}/releases/${releaseId}"
            ) {
                parameter("releaseId", releaseId)
                parameter("token", Network.Discogs.API_KEY)
            }
        }
    }

    override suspend fun searchSongPreview(
        query: String?,
        limit: Int
    ): ResultWrapper<GetSearchResponse> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Deezer.BASE_URL}${Network.Deezer.SEARCH}"
            ) {
                parameter("q", query)
                parameter("limit", limit)
            }
        }
    }

}