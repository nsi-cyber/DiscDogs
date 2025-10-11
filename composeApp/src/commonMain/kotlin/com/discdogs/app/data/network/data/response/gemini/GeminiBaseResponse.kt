package com.discdogs.app.data.network.data.response.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiBaseResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)

@Serializable
data class Content(
    val parts: List<Part>? = null
)

@Serializable
data class Part(
    val text: String? = null
)

@Serializable
data class GeminiResponse(
    @SerialName("name")
    val name: String?,
    @SerialName("response")
    val response: Int?
)