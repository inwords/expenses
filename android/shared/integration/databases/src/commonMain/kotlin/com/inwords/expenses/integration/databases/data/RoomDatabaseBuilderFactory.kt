package com.inwords.expenses.integration.databases.data

import androidx.room.RoomDatabase

internal expect class RoomDatabaseBuilderFactory {

    fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}