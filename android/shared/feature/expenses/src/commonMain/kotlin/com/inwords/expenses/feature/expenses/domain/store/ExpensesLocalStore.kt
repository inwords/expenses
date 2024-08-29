package com.inwords.expenses.feature.expenses.domain.store

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow

internal interface ExpensesLocalStore {

    fun getExpensesFlow(eventId: Long): Flow<List<Expense>>

    suspend fun getExpenses(eventId: Long): List<Expense>

    suspend fun insert(event: Event, expense: Expense): Expense

    suspend fun insert(event: Event, expenses: List<Expense>): List<Expense>

    suspend fun updateExpenseServerId(expenseId: Long, serverId: Long): Boolean
}