package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = EventPersonCrossRef.TABLE_NAME,
    primaryKeys = [
        EventEntity.ColumnNames.ID,
        PersonEntity.ColumnNames.ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [EventEntity.ColumnNames.ID],
            childColumns = [EventEntity.ColumnNames.ID],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = [PersonEntity.ColumnNames.ID],
            childColumns = [PersonEntity.ColumnNames.ID],
            onDelete = ForeignKey.RESTRICT,
        )
    ],
    indices = [
        Index(value = [EventEntity.ColumnNames.ID]),
        Index(value = [PersonEntity.ColumnNames.ID])
    ]
)
data class EventPersonCrossRef(
    @ColumnInfo(name = EventEntity.ColumnNames.ID)
    val eventId: Long,

    @ColumnInfo(name = PersonEntity.ColumnNames.ID)
    val personId: Long,
) {
    companion object {

        const val TABLE_NAME = "event_person_cross_ref"
    }

}