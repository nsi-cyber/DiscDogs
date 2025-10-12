package com.discdogs.app.data.network

import com.nsicyber.vinylscan.BuildConfig

actual object NetworkConfig {
    actual val geminiApiKey: String
        get() = BuildConfig.GEMINI_API_KEY

    actual val discogsApiKey: String
        get() = BuildConfig.DISCOGS_API_KEY
}
