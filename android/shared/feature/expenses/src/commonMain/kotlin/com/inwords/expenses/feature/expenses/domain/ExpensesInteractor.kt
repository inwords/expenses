package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpensesInteractor internal constructor(
    expensesLocalStoreLazy: Lazy<ExpensesLocalStore>,
) {

    private val expensesLocalStore by expensesLocalStoreLazy

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

    suspend fun addExpense(event: Event, expense: Expense) {
        expensesLocalStore.insert(event, expense)
    }

}