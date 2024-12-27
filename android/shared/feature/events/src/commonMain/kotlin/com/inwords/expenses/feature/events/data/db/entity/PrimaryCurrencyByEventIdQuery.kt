package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Relation

data class PrimaryCurrencyByEventIdQuery(

    @ColumnInfo(EventEntity.ColumnNames.PRIMARY_CURRENCY)
    val primaryCurrencyId: Long,

    @Relation(
        entity = CurrencyEntity::class,
        parentColumn = EventEntity.ColumnNames.PRIMARY_CURRENCY,
        entityColumn = CurrencyEntity.ColumnNames.ID
    )
    val primaryCurrency: CurrencyEntity
)
