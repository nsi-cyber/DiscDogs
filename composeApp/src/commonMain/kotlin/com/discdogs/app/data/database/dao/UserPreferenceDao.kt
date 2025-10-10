package com.discdogs.app.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.discdogs.app.data.database.model.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Dao
interface UserPreferenceDao {

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreference(): Flow<UserPreference?>

    @Query("SELECT isFirstTime FROM user_preferences WHERE id = 1")
    suspend fun isFirstTime(): Boolean?

    @Query("SELECT selectedLanguage FROM user_preferences WHERE id = 1")
    suspend fun getSelectedLanguage(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreference(userPreference: UserPreference)

    @Query("UPDATE user_preferences SET isFirstTime = :isFirstTime, lastUpdatedAt = :timestamp WHERE id = 1")
    suspend fun updateFirstTime(
        isFirstTime: Boolean,
        timestamp: Long = System.now().toEpochMilliseconds()
    )

    @Query("UPDATE user_preferences SET selectedLanguage = :language, lastUpdatedAt = :timestamp WHERE id = 1")
    suspend fun updateSelectedLanguage(
        language: String,
        timestamp: Long = System.now().toEpochMilliseconds()
    )

    // Rating-related queries
    @Query("SELECT lastRatingPromptTime FROM user_preferences WHERE id = 1")
    suspend fun getLastRatingPromptTime(): Long?

    @Query("SELECT ratingDialogDismissed FROM user_preferences WHERE id = 1")
    suspend fun isRatingDialogDismissed(): Boolean?

    @Query("SELECT hasRated FROM user_preferences WHERE id = 1")
    suspend fun hasUserRated(): Boolean?

    @Query("UPDATE user_preferences SET lastRatingPromptTime = :timestamp, lastUpdatedAt = :lastUpdated WHERE id = 1")
    suspend fun updateLastRatingPromptTime(
        timestamp: Long,
        lastUpdated: Long = System.now().toEpochMilliseconds()
    )

    @Query("UPDATE user_preferences SET ratingDialogDismissed = :dismissed, lastUpdatedAt = :timestamp WHERE id = 1")
    suspend fun setRatingDialogDismissed(
        dismissed: Boolean,
        timestamp: Long = System.now().toEpochMilliseconds()
    )

    @Query("UPDATE user_preferences SET hasRated = :hasRated, lastUpdatedAt = :timestamp WHERE id = 1")
    suspend fun setUserHasRated(
        hasRated: Boolean,
        timestamp: Long = System.now().toEpochMilliseconds()
    )
}