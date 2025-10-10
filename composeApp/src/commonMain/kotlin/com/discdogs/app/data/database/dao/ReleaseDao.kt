package com.discdogs.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.discdogs.app.data.database.model.Release
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleaseDao {

    @Query("SELECT * FROM releases WHERE id = :releaseId")
    suspend fun getReleaseById(releaseId: Int): Release?

    @Query("SELECT * FROM releases WHERE id = :releaseId")
    fun getReleaseByIdFlow(releaseId: Int): Flow<Release?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelease(release: Release)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReleases(releases: List<Release>)

    @Delete
    suspend fun deleteRelease(release: Release)

    @Query("DELETE FROM releases WHERE id = :releaseId")
    suspend fun deleteReleaseById(releaseId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM releases WHERE id = :releaseId)")
    fun isReleaseExists(releaseId: Int): Flow<Boolean>
}


