package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = EventCurrencyCrossRef.TABLE_NAME,
    primaryKeys = [
        EventEntity.ColumnNames.ID,
        CurrencyEntity.ColumnNames.ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [EventEntity.ColumnNames.ID],
            childColumns = [EventEntity.ColumnNames.ID],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = [CurrencyEntity.ColumnNames.ID],
            childColumns = [CurrencyEntity.ColumnNames.ID],
            onDelete = ForeignKey.RESTRICT,
        )
    ],
    indices = [
        Index(value = [EventEntity.ColumnNames.ID]),
        Index(value = [CurrencyEntity.ColumnNames.ID])
    ]
)
data class EventCurrencyCrossRef(
    @ColumnInfo(name = EventEntity.ColumnNames.ID)
    val eventId: Long,

    @ColumnInfo(name = CurrencyEntity.ColumnNames.ID)
    val currencyId: Long,
) {
    companion object {

        const val TABLE_NAME = "event_currency_cross_ref"
    }

}