package com.discdogs.app.presentation.model


data class PlayingItem(
    val id: String,
    val title: String,
    val image: String,
    val preview: String? = null,
    val progress: Float = 0f,
)