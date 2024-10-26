package com.inwords.expenses.feature.expenses.data.db

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.expenses.data.db.converter.toDomain
import com.inwords.expenses.feature.expenses.data.db.converter.toEntity
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class ExpensesLocalStoreImpl(
    expensesDaoLazy: Lazy<ExpensesDao>
) : ExpensesLocalStore {

    private val expensesDao by expensesDaoLazy

    override fun getExpensesFlow(eventId: Long): Flow<List<Expense>> {
        return expensesDao.queryByEventIdFlow(eventId).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }.distinctUntilChanged()
    }

    override suspend fun getExpenses(eventId: Long): List<Expense> {
        return expensesDao.queryByEventId(eventId).map { entity -> entity.toDomain() }
    }

    override suspend fun insert(event: Event, expense: Expense): Expense {
        val id = expensesDao.insert(
            expenseEntity = expense.toEntity(event),
            subjectPersonSplitEntities = expense.subjecExpenseSplitWithPersons.map { it.toEntity() },
        )

        return id.takeIf { it != -1L }?.let { expense.copy(expenseId = it) } ?: expense
    }

    override suspend fun insert(event: Event, expenses: List<Expense>): List<Expense> {
        return expenses.map { expense -> insert(event, expense) }
    }

    override suspend fun updateExpenseServerId(expenseId: Long, serverId: Long): Boolean {
        return expensesDao.update(expenseId, serverId) >= 1
    }

}