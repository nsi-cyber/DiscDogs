package com.discdogs.app.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "release_list_releases",
    foreignKeys = [
        ForeignKey(
            entity = ReleaseList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listId"]), Index(value = ["releaseId"])]
)
data class ReleaseListRelease @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val releaseId: Int,
    val addedAt: Long = System.now().toEpochMilliseconds()

)


