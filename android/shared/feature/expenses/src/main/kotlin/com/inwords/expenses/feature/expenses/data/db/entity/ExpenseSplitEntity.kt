package com.inwords.expenses.feature.expenses.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inwords.expenses.core.storage.utils.type_converter.BigIntegerConverter
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import java.math.BigInteger

@Entity(
    tableName = ExpenseSplitEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = [ExpenseEntity.ColumnNames.ID],
            childColumns = [ExpenseSplitEntity.ColumnNames.EXPENSE_ID],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = [PersonEntity.ColumnNames.ID],
            childColumns = [ExpenseSplitEntity.ColumnNames.PERSON_ID],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = [ExpenseSplitEntity.ColumnNames.PERSON_ID]),
    ]
)
@TypeConverters(BigIntegerConverter::class)
data class ExpenseSplitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(ColumnNames.ID)
    val expenseSplitId: Long = 0L,

    @ColumnInfo(ColumnNames.EXPENSE_ID)
    val expenseId: Long,

    @ColumnInfo(ColumnNames.PERSON_ID)
    val personId: Long,

    @ColumnInfo(ColumnNames.AMOUNT_UNSCALED)
    val amountUnscaled: BigInteger,

    @ColumnInfo(ColumnNames.SCALE)
    val amountScale: Int,
) {

    companion object {

        const val TABLE_NAME = "expense_split"
    }

    object ColumnNames {

        const val ID = "expense_split_id"
        const val EXPENSE_ID = "expense_id"
        const val PERSON_ID = "person_id"
        const val AMOUNT_UNSCALED = "amount_unscaled"
        const val SCALE = "scale"
    }
}