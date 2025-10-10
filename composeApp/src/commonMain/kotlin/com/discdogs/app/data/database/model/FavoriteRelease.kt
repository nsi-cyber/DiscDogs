package com.discdogs.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.discdogs.app.presentation.model.VinylResultUiModel
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime


fun FavoriteRelease.toUiModel(): VinylResultUiModel {
    return VinylResultUiModel(
        id = id,
        title = title,
        thumb = thumb.orEmpty(),
        year = year?.toString().orEmpty(),
        format = formats,
        genre = genres
    )
}

@Entity(tableName = "favorite_releases")
data class FavoriteRelease @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val thumb: String? = null,
    val artists: List<String> = emptyList(),
    val year: Int? = null,
    val genres: List<String> = emptyList(),
    val country: String? = null,
    val released: String? = null,
    val masterId: Int,
    val formats: List<String> = emptyList(),
    val labels: List<String> = emptyList(),
    val addedAt: Long = System.now().toEpochMilliseconds()

)