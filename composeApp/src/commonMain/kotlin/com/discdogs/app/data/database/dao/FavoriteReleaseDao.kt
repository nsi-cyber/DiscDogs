package com.discdogs.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.discdogs.app.data.database.model.FavoriteRelease
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteReleaseDao {
    @Query("SELECT * FROM favorite_releases ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteRelease>>

    @Query("SELECT * FROM favorite_releases WHERE id = :releaseId")
    fun getFavoriteById(releaseId: Int): Flow<FavoriteRelease?>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_releases WHERE id = :releaseId)")
    fun isFavorite(releaseId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteRelease)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteRelease)

    @Query("DELETE FROM favorite_releases WHERE id = :releaseId")
    suspend fun deleteFavoriteById(releaseId: Int)

    @Query("SELECT COUNT(*) FROM favorite_releases")
    fun getFavoriteCount(): Flow<Int>
} 