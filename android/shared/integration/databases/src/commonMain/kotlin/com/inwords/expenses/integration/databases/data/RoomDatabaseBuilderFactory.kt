package com.inwords.expenses.integration.databases.data

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

internal expect class RoomDatabaseBuilderFactory {

    fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {

    override fun initialize(): AppDatabase
}
