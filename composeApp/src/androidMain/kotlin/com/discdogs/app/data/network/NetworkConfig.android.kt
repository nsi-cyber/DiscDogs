package com.discdogs.app.data.network

import com.discdogs.app.BuildConfig

actual object NetworkConfig {
    actual val geminiApiKey: String
        get() = BuildConfig.GEMINI_API_KEY

    actual val discogsApiKey: String
        get() = BuildConfig.DISCOGS_API_KEY
}
