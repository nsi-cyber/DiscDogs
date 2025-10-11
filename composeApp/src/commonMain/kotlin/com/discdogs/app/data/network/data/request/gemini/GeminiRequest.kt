package com.discdogs.app.data.network.data.request.gemini

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val inline_data: InlineData? = null,
    val text: String? = null
)

@Serializable
data class InlineData(
    val mime_type: String,
    val data: String
)
