package com.inwords.expenses.feature.expenses.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseSubjectPersonCrossRef
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseWithDetailsQuery
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    suspend fun insert(expenseEntity: ExpenseEntity, subjectPersons: List<Person>) {
        val expenseEntityId = insert(expenseEntity)
        insert(
            subjectPersons.map { person ->
                ExpenseSubjectPersonCrossRef(
                    expenseId = expenseEntityId,
                    personId = person.id
                )
            }
        )
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subjectPersonsCrossRef: List<ExpenseSubjectPersonCrossRef>)

    @Transaction
    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME} WHERE ${ExpenseEntity.ColumnNames.EVENT_ID} = :eventId")
    fun queryByEventId(eventId: Long): Flow<List<ExpenseWithDetailsQuery>>

}