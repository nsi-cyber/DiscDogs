package com.discdogs.app.domain

import com.discdogs.app.core.data.Resource
import com.discdogs.app.data.network.data.response.discogs.getMastersVersions.GetMastersVersionsResponse
import com.discdogs.app.data.network.data.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.discdogs.app.data.network.data.response.discogs.getSearch.GetDiscogsSearchResponse
import com.discdogs.app.presentation.model.ExternalWebsites

interface ExternalRepository {

    suspend fun openExternalPlayerLink(
        query: String,
        type: ExternalWebsites
    ): String


}