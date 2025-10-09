package com.discdogs.app.core.audioPlayer

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.isPlaybackLikelyToKeepUp
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual class AudioPlayer actual constructor(private val playerStateFlow: MutableStateFlow<PlaybackState>) {

    private val avAudioPlayer: AVPlayer = AVPlayer()

    @OptIn(ExperimentalForeignApi::class)
    private val observer: (CValue<CMTime>) -> Unit = { time: CValue<CMTime> ->
        val isBuffering = avAudioPlayer.currentItem?.isPlaybackLikelyToKeepUp() != true
        val isPlaying = avAudioPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying

        val rawTime: Float64 = CMTimeGetSeconds(time)
        val parsedTime = rawTime.toDuration(DurationUnit.SECONDS).inWholeSeconds

        val duration = if (avAudioPlayer.currentItem != null) {
            val cmTime = CMTimeGetSeconds(avAudioPlayer.currentItem!!.duration)
            if (cmTime.isNaN()) 0 else cmTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
        } else 0

        if (isBuffering) playerStateFlow.value = PlaybackState.BUFFERING
    }

    init {
        setUpAudioSession()
    }

    actual fun play(url: String) {
        val nsUrl = NSURL.URLWithString(URLString = url)!!
        val playerItem = AVPlayerItem(uRL = nsUrl)

        avAudioPlayer.replaceCurrentItemWithPlayerItem(playerItem)
        startTimeObserver()
        avAudioPlayer.play()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun stop() {
        avAudioPlayer.pause()
        avAudioPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
        playerStateFlow.value = PlaybackState.ENDED
    }

    actual fun cleanUp() {
        avAudioPlayer.pause()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setUpAudioSession() {
        try {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
            audioSession.setActive(true, null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startTimeObserver() {
        CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())

        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = avAudioPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                playerStateFlow.value = PlaybackState.ENDED

            }
        )
    }


}