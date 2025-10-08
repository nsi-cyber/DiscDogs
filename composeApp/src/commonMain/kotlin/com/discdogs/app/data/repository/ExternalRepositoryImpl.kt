package com.discdogs.app.data.repository

import androidx.compose.ui.platform.LocalUriHandler
import com.discdogs.app.domain.ExternalRepository
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.toUrl
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest


class ExternalRepositoryImpl() : ExternalRepository {
    val externalPlayerOpener = ExternalPlayerOpener()

    override suspend fun openExternalPlayerLink(
        query: String,
        type: ExternalWebsites
    ): String {


        val fallbackUrl = "https://www.google.com/search?q=site:${type.toUrl()}+${query}"

        val resolvedUrl = try {
            val searchUrl = "$fallbackUrl&btnI=1"
            val doc=Ksoup.parseGetRequest(searchUrl)

                val doc1 = doc.baseUri().replace("https://www.google.com/url?q=", "")

            doc1
        } catch (e: Exception) {
            fallbackUrl
        }

        return resolvedUrl
/*
        try {
            externalPlayerOpener.openUrl(finalUrl)
        } catch (e: Exception) {

        }


 */

    }

}