package com.discdogs.app.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.discdogs.app.data.database.dao.FavoriteReleaseDao
import com.discdogs.app.data.database.dao.RecentReleaseDao
import com.discdogs.app.data.database.dao.ReleaseDao
import com.discdogs.app.data.database.dao.ReleaseListDao
import com.discdogs.app.data.database.dao.ReleaseListReleaseDao
import com.discdogs.app.data.database.dao.UserPreferenceDao
import com.discdogs.app.data.database.model.FavoriteRelease
import com.discdogs.app.data.database.model.RecentRelease
import com.discdogs.app.data.database.model.Release
import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.data.database.model.ReleaseListRelease
import com.discdogs.app.data.database.model.UserPreference

@Database(
    entities = [BookEntity::class,
        FavoriteRelease::class,
        RecentRelease::class,
        UserPreference::class,
        Release::class,
        ReleaseList::class,
        ReleaseListRelease::class],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class FavoriteBookDatabase: RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao
    abstract val favoriteReleaseDao: FavoriteReleaseDao
    abstract val recentReleaseDao: RecentReleaseDao
    abstract val userPreferenceDao: UserPreferenceDao
    abstract val releaseDao: ReleaseDao
    abstract val releaseListDao: ReleaseListDao
    abstract val releaseListReleaseDao: ReleaseListReleaseDao

    companion object {
        const val DB_NAME = "discdogs.db"
    }
}