package com.discdogs.app.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.discdogs.app.data.database.migrations.DatabaseMigrations

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<VinylDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(VinylDatabase.DB_NAME)
        return Room.databaseBuilder<VinylDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).addMigrations(
            DatabaseMigrations.MIGRATION_1_2,
            DatabaseMigrations.MIGRATION_2_3,
            DatabaseMigrations.MIGRATION_3_4
        )
    }
}