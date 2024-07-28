package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDao {

    @Upsert
    suspend fun insert(currencyEntities: List<CurrencyEntity>): List<Long>

    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME}")
    fun queryAll(): Flow<List<CurrencyEntity>>

}