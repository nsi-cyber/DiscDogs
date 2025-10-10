package com.discdogs.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@Entity(tableName = "release_lists")
data class ReleaseList @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,

    val createdAt: Long = System.now().toEpochMilliseconds(),
    val updatedAt: Long = System.now().toEpochMilliseconds()
)


