package com.discdogs.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.discdogs.app.data.database.model.ReleaseList
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleaseListDao {

    @Query("SELECT * FROM release_lists ORDER BY updatedAt DESC")
    fun getAllLists(): Flow<List<ReleaseList>>

    @Query("SELECT * FROM release_lists WHERE id = :listId")
    suspend fun getListById(listId: Long): ReleaseList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ReleaseList): Long

    @Update
    suspend fun updateList(list: ReleaseList)

    @Delete
    suspend fun deleteList(list: ReleaseList)

    @Query("DELETE FROM release_lists WHERE id = :listId")
    suspend fun deleteListById(listId: Long)
}


