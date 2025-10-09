package com.discdogs.app.core.audioPlayer

import kotlinx.coroutines.flow.MutableStateFlow

expect class AudioPlayer(playerStateFlow: MutableStateFlow<PlaybackState>) {
    fun play(url: String)
    fun stop()
    fun cleanUp()
}