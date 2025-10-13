package com.discdogs.app.data.network

import com.discdogs.app.core.data.ResultWrapper
import com.discdogs.app.core.data.safeApiCall
import com.discdogs.app.data.network.data.request.gemini.Content
import com.discdogs.app.data.network.data.request.gemini.GeminiRequest
import com.discdogs.app.data.network.data.request.gemini.InlineData
import com.discdogs.app.data.network.data.request.gemini.Part
import com.discdogs.app.data.network.data.response.base.PaginationBaseResponse
import com.discdogs.app.data.network.data.response.deezer.search.GetSearchResponse
import com.discdogs.app.data.network.data.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.data.network.data.response.gemini.GeminiBaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.io.encoding.Base64


object Network {
    object Discogs {
        const val BASE_URL = "https://api.discogs.com"
        val API_KEY: String get() = NetworkConfig.discogsApiKey
        const val SEARCH = "/database/search"
    }

    object Deezer {
        const val BASE_URL = "https://api.deezer.com"
        const val SEARCH = "/search"
    }

    object Gemini {
        const val BASE_URL = "https://generativelanguage.googleapis.com"
        val API_KEY: String get() = NetworkConfig.geminiApiKey
        const val GENERATE_CONTENT = "/v1beta/models/gemini-2.5-flash:generateContent"
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
                parameter("token", Network.Discogs.API_KEY)
            }
        }
    }
    override suspend fun getMasterDetail(masterId: Int?): ResultWrapper<GetMasterDetailResponse> {
        return safeApiCall {
            httpClient.get(
                urlString = "${Network.Discogs.BASE_URL}/masters/${masterId}"
            ) {
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

    override suspend fun generateImageCaption(
        imageBytes: ByteArray,
        prompt: String
    ): ResultWrapper<GeminiBaseResponse> {
        return safeApiCall {
            val base64Image = Base64.encode(imageBytes)
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                inline_data = InlineData(
                                    mime_type = "image/jpeg",
                                    data = base64Image
                                )
                            ),
                            Part(text = prompt)
                        )
                    )
                )
            )

            httpClient.post(
                urlString = "${Network.Gemini.BASE_URL}${Network.Gemini.GENERATE_CONTENT}"
            ) {
                headers {
                    append("x-goog-api-key", Network.Gemini.API_KEY)
                }
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

}