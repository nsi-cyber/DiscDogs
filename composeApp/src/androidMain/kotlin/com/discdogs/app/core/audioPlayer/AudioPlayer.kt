package com.discdogs.app.core.audioPlayer

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.discdogs.app.DiscDogApplication
import kotlinx.coroutines.flow.MutableStateFlow

actual class AudioPlayer actual constructor(private val playerStateFlow: MutableStateFlow<PlaybackState>) {

    private val handler = Handler(Looper.getMainLooper())
    private val mediaPlayer = ExoPlayer.Builder(DiscDogApplication.appContext).build()


    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    playerStateFlow.value = PlaybackState.IDLE
                }

                Player.STATE_BUFFERING -> {
                    playerStateFlow.value = PlaybackState.BUFFERING
                }

                Player.STATE_ENDED -> {
                    playerStateFlow.value = PlaybackState.ENDED
                }

                Player.STATE_READY -> {
                    playerStateFlow.value = PlaybackState.READY
                }
            }
        }


    }

    init {
        mediaPlayer.addListener(listener)
    }

    actual fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        mediaPlayer.setMediaItem(mediaItem)
        mediaPlayer.prepare()
        mediaPlayer.play()
    }

    actual fun stop() {
        mediaPlayer.stop()
        playerStateFlow.value = PlaybackState.ENDED

    }

    actual fun cleanUp() {
        mediaPlayer.release()
        mediaPlayer.removeListener(listener)
    }


}