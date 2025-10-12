package com.discdogs.app.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SQLiteConnection) {
            // Add new rating-related columns to user_preferences table
            database.execSQL(
                """
                ALTER TABLE user_preferences 
                ADD COLUMN lastRatingPromptTime INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )

            database.execSQL(
                """
                ALTER TABLE user_preferences 
                ADD COLUMN ratingDialogDismissed INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )

            database.execSQL(
                """
                ALTER TABLE user_preferences 
                ADD COLUMN hasRated INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SQLiteConnection) {
            // Create release_lists table
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS release_lists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL
                )
                """.trimIndent()
            )

            // Create release_list_releases table
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS release_list_releases (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    listId INTEGER NOT NULL,
                    releaseId INTEGER NOT NULL,
                    addedAt INTEGER NOT NULL,
                    FOREIGN KEY(listId) REFERENCES release_lists(id) ON DELETE CASCADE
                )
                """.trimIndent()
            )

            // Create indices for better performance
            database.execSQL("CREATE INDEX IF NOT EXISTS index_release_list_releases_listId ON release_list_releases (listId)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_release_list_releases_releaseId ON release_list_releases (releaseId)")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SQLiteConnection) {
            // Create releases table to store release details independently
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS releases (
                    id INTEGER PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    thumb TEXT,
                    artists TEXT NOT NULL DEFAULT '[]',
                    year INTEGER,
                    genres TEXT NOT NULL DEFAULT '[]',
                    country TEXT,
                    released TEXT,
                    masterId INTEGER NOT NULL,
                    formats TEXT NOT NULL DEFAULT '[]',
                    labels TEXT NOT NULL DEFAULT '[]',
                    addedAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }
}
