package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class ExpensesInteractor internal constructor(
    expensesLocalStoreLazy: Lazy<ExpensesLocalStore>,
) {

    private val _refreshExpenses = MutableSharedFlow<Event>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val refreshExpenses: Flow<Event> = _refreshExpenses

    private val expensesLocalStore by expensesLocalStoreLazy

    fun getExpensesFlow(eventId: Long): Flow<List<Expense>> {
        return expensesLocalStore.getExpensesFlow(eventId)
    }

    internal fun getExpensesDetails(eventDetails: EventDetails): Flow<ExpensesDetails> {
        return expensesLocalStore.getExpensesFlow(eventDetails.event.id)
            .map { expenses ->
                val debtCalculator = DebtCalculator(expenses, eventDetails.primaryCurrency)

                ExpensesDetails(
                    event = eventDetails,
                    expenses = expenses,
                    debtCalculator = debtCalculator,
                )
            }
    }

    internal suspend fun addExpense(event: Event, expense: Expense) {
        expensesLocalStore.insert(event, expense)
    }

    internal suspend fun onRefreshExpensesAsync(event: Event) {
        _refreshExpenses.emit(event)
    }

}