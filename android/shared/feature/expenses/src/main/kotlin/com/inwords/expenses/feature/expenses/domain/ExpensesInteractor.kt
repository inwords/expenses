package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpensesInteractor internal constructor(
    private val expensesRepository: ExpensesRepository,
) {

    internal fun getExpensesDetails(eventDetails: EventDetails): Flow<ExpensesDetails> {
        return expensesRepository.getExpenses(eventDetails)
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
        expensesRepository.insert(event, expense)
    }

}