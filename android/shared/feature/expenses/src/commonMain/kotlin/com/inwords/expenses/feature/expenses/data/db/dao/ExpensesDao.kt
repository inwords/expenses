package com.inwords.expenses.feature.expenses.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseSplitEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseWithDetailsQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    suspend fun insert(expenseEntity: ExpenseEntity, subjectPersonSplitEntities: List<ExpenseSplitEntity>) {
        val expenseId = insert(expenseEntity)

        insert(
            subjectPersonSplitEntities.map { personSplit ->
                personSplit.copy(expenseId = expenseId)
            }
        )
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personSplitEntities: List<ExpenseSplitEntity>)

    @Transaction
    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME} WHERE ${ExpenseEntity.ColumnNames.EVENT_ID} = :eventId")
    fun queryByEventId(eventId: Long): Flow<List<ExpenseWithDetailsQuery>>

}