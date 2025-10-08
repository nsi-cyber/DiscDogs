package com.discdogs.app.core.audioPlayer

import kotlinx.cinterop.*
import kotlinx.coroutines.flow.MutableStateFlow
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.*
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.*
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual class AudioPlayer actual constructor(private val playerStateFlow: MutableStateFlow<PlayerState>) {
    
    private val avAudioPlayer: AVPlayer = AVPlayer()
    private lateinit var timeObserver: Any
    
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
        
        playerStateFlow.value = playerStateFlow.value.copy(
            isBuffering = isBuffering,
            isPlaying = isPlaying,
            currentTime = parsedTime,
            duration = duration
        )
    }
    
    init {
        setUpAudioSession()
        playerStateFlow.value = playerStateFlow.value.copy(
            isPlaying = avAudioPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
        )
    }
    
    actual fun play(url: String) {
        playerStateFlow.value = playerStateFlow.value.copy(
            isFinished = false,
            isBuffering = true
        )
        
        val nsUrl = NSURL.URLWithString(URLString = url)!!
        val playerItem = AVPlayerItem(uRL = nsUrl)
        
        avAudioPlayer.replaceCurrentItemWithPlayerItem(playerItem)
        startTimeObserver()
        avAudioPlayer.play()
    }
    
    @OptIn(ExperimentalForeignApi::class)
    actual fun stop() {
        stopTimeObserver()
        avAudioPlayer.pause()
        avAudioPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
        playerStateFlow.value = playerStateFlow.value.copy(
            currentTime = 0,
            isPlaying = false
        )
    }
    
    actual fun cleanUp() {
        stopTimeObserver()
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
        val interval = CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())
        timeObserver = avAudioPlayer.addPeriodicTimeObserverForInterval(interval, null, observer)
        
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = avAudioPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                playerStateFlow.value = playerStateFlow.value.copy(
                    isFinished = true,
                    isPlaying = false
                )
                stopTimeObserver()
            }
        )
    }
    
    @OptIn(ExperimentalForeignApi::class)
    private fun stopTimeObserver() {
        if (::timeObserver.isInitialized) {
            avAudioPlayer.removeTimeObserver(timeObserver)
        }
    }
}