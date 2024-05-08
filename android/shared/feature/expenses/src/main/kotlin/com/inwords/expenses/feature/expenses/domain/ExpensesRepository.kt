package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow

internal interface ExpensesRepository {

    fun getExpenses(event: EventDetails): Flow<List<Expense>>

    suspend fun insert(event: Event, expense: Expense)

}