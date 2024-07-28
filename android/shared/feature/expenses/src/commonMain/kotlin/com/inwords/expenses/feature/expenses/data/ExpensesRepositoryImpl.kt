package com.inwords.expenses.feature.expenses.data

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.data.db.converter.toDomain
import com.inwords.expenses.feature.expenses.data.db.converter.toEntity
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.domain.ExpensesRepository
import com.inwords.expenses.feature.expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ExpensesRepositoryImpl(
    expensesDaoLazy: Lazy<ExpensesDao>
) : ExpensesRepository {

    private val expensesDao by expensesDaoLazy

    override fun getExpenses(event: EventDetails): Flow<List<Expense>> {
        return expensesDao.queryByEventId(event.event.id).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun insert(event: Event, expense: Expense) {
        expensesDao.insert(
            expenseEntity = expense.toEntity(event),
            subjectPersonSplitEntities = expense.subjecExpenseSplitWithPersons.map { it.toEntity() },
        )
    }

}
