package com.inwords.expenses.feature.expenses.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity

data class ExpenseSplitWithPersonQuery(

    @Embedded
    val expenseSplitEntity: ExpenseSplitEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = ExpenseEntity.ColumnNames.PERSON_ID,
        entityColumn = PersonEntity.ColumnNames.ID,
    )
    val person: PersonEntity,
)
