package com.discdogs.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@Entity(tableName = "user_preferences")
data class UserPreference @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey
    val id: Int = 1, // Always use id=1 for singleton
    val isFirstTime: Boolean = true,
    val selectedLanguage: String = "en", // Default to English
    val lastUpdatedAt: Long = System.now().toEpochMilliseconds(),

    // Rating dialog properties
    val lastRatingPromptTime: Long = 0L, // Timestamp of last rating prompt
    val ratingDialogDismissed: Boolean = false, // User clicked "I don't want"
    val hasRated: Boolean = false // User has already rated the app
)