package com.discdogs.app.core.audioPlayer

import androidx.compose.runtime.*

@Stable
data class PlayerState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isFinished: Boolean = false,
    val currentTime: Long = 0,
    val duration: Long = 0
)

@Composable
fun rememberPlayerState(): PlayerState {
    return remember {
        PlayerState()
    }
}