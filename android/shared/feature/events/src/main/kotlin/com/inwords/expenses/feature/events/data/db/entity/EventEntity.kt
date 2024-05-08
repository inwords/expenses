package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = EventEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = [CurrencyEntity.ColumnNames.ID],
            childColumns = [EventEntity.ColumnNames.PRIMARY_CURRENCY],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = [PersonEntity.ColumnNames.ID],
            childColumns = [EventEntity.ColumnNames.PRIMARY_PERSON_ID],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = [EventEntity.ColumnNames.PRIMARY_CURRENCY]),
        Index(value = [EventEntity.ColumnNames.PRIMARY_PERSON_ID])
    ]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(ColumnNames.ID)
    val eventId: Long = 0L,

    @ColumnInfo(ColumnNames.NAME)
    val name: String,

    @ColumnInfo(ColumnNames.PRIMARY_CURRENCY)
    val primaryCurrencyId: Long,

    @ColumnInfo(ColumnNames.PRIMARY_PERSON_ID)
    val primaryPersonId: Long,
) {
    companion object {

        const val TABLE_NAME = "event"
    }

    object ColumnNames {

        const val ID = "event_id"
        const val NAME = "name"
        const val PRIMARY_CURRENCY = "primary_currency_id"
        const val PRIMARY_PERSON_ID = "primary_person_id"
    }
}