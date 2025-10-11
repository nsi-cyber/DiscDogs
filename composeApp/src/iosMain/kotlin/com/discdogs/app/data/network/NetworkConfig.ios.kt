package com.discdogs.app.data.network

import platform.Foundation.NSBundle

actual object NetworkConfig {
    actual val geminiApiKey: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("GEMINI_API_KEY") as? String
            ?: "YOUR_GEMINI_API_KEY_HERE"

    actual val discogsApiKey: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("DISCOGS_API_KEY") as? String
            ?: "YOUR_DISCOGS_API_KEY_HERE"
}
