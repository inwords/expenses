package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpensesInteractor internal constructor(
    private val eventsInteractor: EventsInteractor,
    private val expensesRepository: ExpensesRepository,
) {

    fun getExpensesDetails(event: Event): Flow<ExpensesDetails> {
        return eventsInteractor.getEventDetails(event)
            .flatMapLatestNoBuffer { eventDetails ->
                expensesRepository.getExpenses(eventDetails)
                    .map { expenses ->
                        ExpensesDetails(
                            event = eventDetails,
                            expenses = expenses,
                        )
                    }
            }
    }

    suspend fun addExpense(event: Event, expense: Expense) {
        expensesRepository.insert(event, expense)
    }

}