package com.discdogs.app.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object VinylDatabaseConstructor : RoomDatabaseConstructor<VinylDatabase> {
    override fun initialize(): VinylDatabase
}