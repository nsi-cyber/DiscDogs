package com.discdogs.app.core.audioPlayer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AudioRepository {
    val playerState: StateFlow<PlayerState>
    fun play(url: String)
    fun stop()
    fun cleanup()
}

class AudioRepositoryImpl : AudioRepository {
    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    
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
