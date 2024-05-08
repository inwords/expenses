package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EventWithDetailsQuery(

    @Embedded
    val event: EventEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = EventEntity.ColumnNames.ID,
        entityColumn = PersonEntity.ColumnNames.ID,
        associateBy = Junction(EventPersonCrossRef::class)
    )
    val persons: List<PersonEntity>,

    @Relation(
        entity = CurrencyEntity::class,
        parentColumn = EventEntity.ColumnNames.ID,
        entityColumn = CurrencyEntity.ColumnNames.ID,
        associateBy = Junction(EventCurrencyCrossRef::class)
    )
    val currencies: List<CurrencyEntity>
)
