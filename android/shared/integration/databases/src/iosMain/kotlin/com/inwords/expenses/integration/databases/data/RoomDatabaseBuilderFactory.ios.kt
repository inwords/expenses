package com.inwords.expenses.integration.databases.data

import androidx.room.Room
import androidx.room.RoomDatabase

internal actual class RoomDatabaseBuilderFactory {

    actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = NSHomeDirectory() + "/app_db.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
            factory = { AppDatabase::class.instantiateImpl() }
        )
    }
}