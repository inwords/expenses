package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.EventWithDetailsQuery

@Dao
interface EventsDao {

    @Upsert
    suspend fun insert(eventEntity: EventEntity)

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME}")
    suspend fun queryAll(): List<EventEntity>

    @Transaction
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE ${EventEntity.ColumnNames.ID} = :eventId")
    suspend fun queryEventWithDetailsById(eventId: Long): EventWithDetailsQuery

}