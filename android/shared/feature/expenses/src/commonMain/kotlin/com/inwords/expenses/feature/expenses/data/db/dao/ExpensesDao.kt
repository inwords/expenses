package com.inwords.expenses.feature.expenses.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseSplitEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseWithDetailsQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    suspend fun insert(expenseEntity: ExpenseEntity, subjectPersonSplitEntities: List<ExpenseSplitEntity>): Long {
        val expenseId = insert(expenseEntity)

        insert(
            subjectPersonSplitEntities.map { personSplit ->
                personSplit.copy(expenseId = expenseId)
            }
        )

        return expenseId
    }

    @Upsert
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Upsert
    suspend fun insert(personSplitEntities: List<ExpenseSplitEntity>)

    @Transaction
    @Query("UPDATE ${ExpenseEntity.TABLE_NAME} SET ${ExpenseEntity.ColumnNames.SERVER_ID} = :serverId " +
        "WHERE ${ExpenseEntity.ColumnNames.ID} = :expenseId")
    suspend fun update(expenseId: Long, serverId: Long): Int

    @Transaction
    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME} WHERE ${ExpenseEntity.ColumnNames.EVENT_ID} = :eventId")
    fun queryByEventIdFlow(eventId: Long): Flow<List<ExpenseWithDetailsQuery>>

    @Transaction
    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME} WHERE ${ExpenseEntity.ColumnNames.EVENT_ID} = :eventId")
    suspend fun queryByEventId(eventId: Long): List<ExpenseWithDetailsQuery>

}