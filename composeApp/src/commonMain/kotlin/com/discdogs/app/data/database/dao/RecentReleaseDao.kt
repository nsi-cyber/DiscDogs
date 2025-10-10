package com.discdogs.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.discdogs.app.data.database.model.RecentRelease
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentReleaseDao {

    @Query("SELECT * FROM recent_releases ORDER BY accessedAt DESC")
    fun getRecentReleases(): Flow<List<RecentRelease>>

    @Query("SELECT * FROM recent_releases WHERE source = :source ORDER BY accessedAt DESC")
    fun getRecentReleasesBySource(source: String): Flow<List<RecentRelease>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentRelease(release: RecentRelease)

    @Query("DELETE FROM recent_releases WHERE id = :releaseId")
    suspend fun deleteRecentRelease(releaseId: Int)

    @Query("DELETE FROM recent_releases WHERE id NOT IN (SELECT id FROM recent_releases ORDER BY accessedAt DESC LIMIT 50)")
    suspend fun cleanupOldReleases()

    @Query("DELETE FROM recent_releases WHERE source = :source")
    suspend fun clearRecentReleasesBySource(source: String)

    @Query("DELETE FROM recent_releases")
    suspend fun clearAllRecentReleases()
} 