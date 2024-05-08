package com.inwords.expenses.feature.expenses.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity

@Entity(
    tableName = ExpenseSubjectPersonCrossRef.TABLE_NAME,
    primaryKeys = [
        ExpenseEntity.ColumnNames.ID,
        PersonEntity.ColumnNames.ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = [ExpenseEntity.ColumnNames.ID],
            childColumns = [ExpenseEntity.ColumnNames.ID],
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
        Index(value = [ExpenseEntity.ColumnNames.ID]),
        Index(value = [PersonEntity.ColumnNames.ID])
    ]
)
data class ExpenseSubjectPersonCrossRef(
    @ColumnInfo(name = ExpenseEntity.ColumnNames.ID)
    val expenseId: Long,

    @ColumnInfo(name = PersonEntity.ColumnNames.ID)
    val personId: Long,
) {
    companion object {

        const val TABLE_NAME = "expense_subject_person_cross_ref"
    }

}