package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.EventWithDetailsQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {

    @Upsert
    suspend fun insert(eventEntity: EventEntity): Long

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME}")
    fun queryAll(): Flow<List<EventEntity>>

    @Transaction
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE ${EventEntity.ColumnNames.ID} = :eventId")
    fun queryEventWithDetailsById(eventId: Long): Flow<EventWithDetailsQuery>

}