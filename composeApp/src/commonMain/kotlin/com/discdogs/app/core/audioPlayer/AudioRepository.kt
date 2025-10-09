package com.discdogs.app.core.audioPlayer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AudioRepository {
    val playerState: StateFlow<PlaybackState>
    fun play(url: String)
    fun stop()
    fun cleanup()
}

class AudioRepositoryImpl : AudioRepository {
    private val _playerState = MutableStateFlow(PlaybackState.IDLE)
    override val playerState: StateFlow<PlaybackState> = _playerState.asStateFlow()
    
    private val audioPlayer = AudioPlayer(_playerState)
    
    override fun play(url: String) {
        audioPlayer.play(url)
    }
    
    override fun stop() {
        audioPlayer.stop()
    }
    
    override fun cleanup() {
        audioPlayer.cleanUp()
    }
}
