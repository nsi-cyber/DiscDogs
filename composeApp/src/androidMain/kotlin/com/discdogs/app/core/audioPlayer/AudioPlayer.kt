package com.discdogs.app.core.audioPlayer

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.discdogs.app.DiscDogsApplication
import kotlinx.coroutines.flow.MutableStateFlow

actual class AudioPlayer actual constructor(private val playerStateFlow: MutableStateFlow<PlayerState>) {
    
    private val handler = Handler(Looper.getMainLooper())
    private val mediaPlayer = ExoPlayer.Builder(DiscDogsApplication.appContext).build()
    
    private val updateRunnable = object : Runnable {
        override fun run() {
            val currentTime = mediaPlayer.currentPosition / 1000
            playerStateFlow.value = playerStateFlow.value.copy(currentTime = currentTime)
            handler.postDelayed(this, 1000)
        }
    }
    
    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    playerStateFlow.value = playerStateFlow.value.copy(
                        isPlaying = false,
                        isBuffering = false
                    )
                }
                Player.STATE_BUFFERING -> {
                    playerStateFlow.value = playerStateFlow.value.copy(isBuffering = true)
                }
                Player.STATE_ENDED -> {
                    playerStateFlow.value = playerStateFlow.value.copy(
                        isPlaying = false,
                        isFinished = true
                    )
                    stopUpdate()
                }
                Player.STATE_READY -> {
                    val duration = mediaPlayer.duration / 1000
                    playerStateFlow.value = playerStateFlow.value.copy(
                        isBuffering = false,
                        duration = duration
                    )
                }
            }
        }
        
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerStateFlow.value = playerStateFlow.value.copy(isPlaying = isPlaying)
            if (isPlaying) {
                scheduleUpdate()
            } else {
                stopUpdate()
            }
        }
    }
    
    init {
        mediaPlayer.addListener(listener)
    }
    
    actual fun play(url: String) {
        playerStateFlow.value = playerStateFlow.value.copy(isFinished = false)
        val mediaItem = MediaItem.fromUri(url)
        mediaPlayer.setMediaItem(mediaItem)
        mediaPlayer.prepare()
        mediaPlayer.play()
    }
    
    actual fun stop() {
        mediaPlayer.stop()
        stopUpdate()
        playerStateFlow.value = playerStateFlow.value.copy(
            currentTime = 0,
            isPlaying = false
        )
    }
    
    actual fun cleanUp() {
        mediaPlayer.release()
        mediaPlayer.removeListener(listener)
    }
    
    private fun stopUpdate() {
        handler.removeCallbacks(updateRunnable)
    }
    
    private fun scheduleUpdate() {
        stopUpdate()
        handler.postDelayed(updateRunnable, 100)
    }
}