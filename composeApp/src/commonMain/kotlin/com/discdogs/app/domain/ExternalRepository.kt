package com.discdogs.app.domain

import com.discdogs.app.presentation.model.ExternalWebsites

interface ExternalRepository {

    suspend fun openExternalPlayerLink(
        query: String,
        type: ExternalWebsites
    ): String


}