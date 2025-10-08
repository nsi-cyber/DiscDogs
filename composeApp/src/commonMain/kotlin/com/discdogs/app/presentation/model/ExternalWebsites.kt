package com.discdogs.app.presentation.model


enum class ExternalWebsites {
    YOUTUBE_MUSIC,
    SPOTIFY,
    APPLE_MUSIC
}

fun ExternalWebsites.toUrl(): String {
    return when (this) {
        ExternalWebsites.YOUTUBE_MUSIC -> "music.youtube.com"
        ExternalWebsites.SPOTIFY -> "spotify.com"
        ExternalWebsites.APPLE_MUSIC -> "music.apple.com"
    }
}