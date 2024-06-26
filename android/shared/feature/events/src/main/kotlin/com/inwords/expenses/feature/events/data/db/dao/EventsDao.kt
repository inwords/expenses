package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.EventCurrencyCrossRef
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.EventPersonCrossRef
import com.inwords.expenses.feature.events.data.db.entity.EventWithDetailsQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {

    @Upsert
    suspend fun insert(eventEntity: EventEntity): Long

    @Insert
    suspend fun insertPersonCrossRef(eventWithPersonCrossRefs: List<EventPersonCrossRef>)

    @Insert
    suspend fun insertCurrencyCrossRef(eventWithCurrencyCrossRefs: List<EventCurrencyCrossRef>)

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME}")
    fun queryAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE ${EventEntity.ColumnNames.ID} = :eventId")
    fun queryById(eventId: Long): Flow<EventEntity>

    @Transaction
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE ${EventEntity.ColumnNames.ID} = :eventId")
    fun queryEventWithDetailsById(eventId: Long): Flow<EventWithDetailsQuery>

}