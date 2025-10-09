package com.discdogs.app.core.audioPlayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


enum class PlaybackState {
    IDLE,
    BUFFERING,
    READY,
    ENDED
}
@Composable
fun rememberPlayerState(): PlaybackState {
    return remember {
        PlaybackState.IDLE
    }
}