package com.inwords.expenses.feature.events.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity

@Dao
interface CurrenciesDao {

    @Upsert
    suspend fun insert(personEntity: CurrencyEntity)

    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME}")
    suspend fun queryAll(): List<CurrencyEntity>

}