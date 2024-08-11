package com.inwords.expenses.integration.databases.data

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

internal expect class RoomDatabaseBuilderFactory {

    fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> // TODO make internal when Room is fixed