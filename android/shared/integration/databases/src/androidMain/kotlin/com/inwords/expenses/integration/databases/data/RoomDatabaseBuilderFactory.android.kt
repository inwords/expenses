package com.inwords.expenses.integration.databases.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

internal actual class RoomDatabaseBuilderFactory(
    private val context: Context
) {

    actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = context.getDatabasePath("app_db.db")
        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
    }
}