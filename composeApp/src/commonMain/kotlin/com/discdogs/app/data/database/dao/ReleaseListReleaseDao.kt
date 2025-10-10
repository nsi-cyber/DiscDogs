package com.discdogs.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.discdogs.app.data.database.model.Release
import com.discdogs.app.data.database.model.ReleaseListRelease
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleaseListReleaseDao {

    @Query("SELECT * FROM release_list_releases WHERE listId = :listId ORDER BY addedAt DESC")
    fun getReleasesByListId(listId: Long): Flow<List<ReleaseListRelease>>

    @Query(
        "SELECT r.* FROM releases r " +
                "INNER JOIN release_list_releases rlr ON r.id = rlr.releaseId " +
                "WHERE rlr.listId = :listId " +
                "ORDER BY rlr.addedAt DESC"
    )
    fun getReleasesInList(listId: Long): Flow<List<Release>>

    @Query("SELECT EXISTS(SELECT 1 FROM release_list_releases WHERE listId = :listId AND releaseId = :releaseId)")
    fun isReleaseInList(listId: Long, releaseId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReleaseToList(releaseListRelease: ReleaseListRelease)

    @Delete
    suspend fun removeReleaseFromList(releaseListRelease: ReleaseListRelease)

    @Query("DELETE FROM release_list_releases WHERE listId = :listId AND releaseId = :releaseId")
    suspend fun removeReleaseFromListById(listId: Long, releaseId: Int)

    @Query("DELETE FROM release_list_releases WHERE listId = :listId")
    suspend fun removeAllReleasesFromList(listId: Long)

    @Query("SELECT COUNT(*) FROM release_list_releases WHERE listId = :listId")
    fun getReleaseCountInList(listId: Long): Flow<Int>

    @Query("SELECT listId FROM release_list_releases WHERE releaseId = :releaseId")
    fun getListsContainingRelease(releaseId: Int): Flow<List<Long>>
}
