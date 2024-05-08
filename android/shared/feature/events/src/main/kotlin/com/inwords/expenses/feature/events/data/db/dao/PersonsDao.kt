package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity

@Dao
interface PersonsDao {

    @Upsert
    suspend fun insert(personEntity: PersonEntity)

    @Query("SELECT * FROM ${PersonEntity.TABLE_NAME}")
    suspend fun queryAll(): List<PersonEntity>

}