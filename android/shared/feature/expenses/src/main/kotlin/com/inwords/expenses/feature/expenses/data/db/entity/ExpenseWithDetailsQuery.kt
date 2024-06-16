package com.inwords.expenses.feature.expenses.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity

data class ExpenseWithDetailsQuery(

    @Embedded
    val expense: ExpenseEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = ExpenseEntity.ColumnNames.PERSON_ID,
        entityColumn = PersonEntity.ColumnNames.ID,
    )
    val person: PersonEntity,

    @Relation(
        entity = ExpenseSplitEntity::class,
        parentColumn = ExpenseSplitEntity.ColumnNames.EXPENSE_ID,
        entityColumn = ExpenseEntity.ColumnNames.ID,
    )
    val expenseSplitWithPersons: List<ExpenseSplitWithPersonQuery>,

    @Relation(
        entity = CurrencyEntity::class,
        parentColumn = ExpenseEntity.ColumnNames.CURRENCY_ID,
        entityColumn = CurrencyEntity.ColumnNames.ID,
    )
    val currency: CurrencyEntity
)
