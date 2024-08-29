package com.inwords.expenses.feature.expenses.domain.tasks

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import com.inwords.expenses.feature.expenses.domain.store.ExpensesRemoteStore
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class EventExpensesPushTask internal constructor(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    expensesLocalStoreLazy: Lazy<ExpensesLocalStore>,
    expensesRemoteStoreLazy: Lazy<ExpensesRemoteStore>,
) {

    private val eventsLocalStore by eventsLocalStoreLazy
    private val expensesLocalStore by expensesLocalStoreLazy
    private val expensesRemoteStore by expensesRemoteStoreLazy

    /**
     * Prerequisites:
     * 1. Currencies are synced
     * 2. Event is synced
     * 3. Persons are synced
     */
    suspend fun pushEventExpenses(eventId: Long): IoResult<*> = withContext(IO) {
        val localEvent = eventsLocalStore.getEventWithDetails(eventId)
            ?.takeIf { details ->
                details.event.serverId != 0L &&
                    details.persons.all { it.serverId != 0L } &&
                    details.currencies.all { it.serverId != 0L }
            } ?: return@withContext IoResult.Error.Failure

        val localExpenses = expensesLocalStore.getExpenses(eventId)
        if (localExpenses.isEmpty()) return@withContext IoResult.Success(Unit)

        val expensesToAdd = localExpenses.filter { it.serverId == 0L }
        if (expensesToAdd.isEmpty()) return@withContext IoResult.Success(Unit)

        val expensesToAddFiltered = expensesToAdd.filter { it.person.serverId != 0L && it.currency.serverId != 0L }
        if (expensesToAddFiltered.isEmpty()) return@withContext IoResult.Success(Unit) // FIXME: non-fatal

        val networkResults = expensesRemoteStore.addExpensesToEvent(
            event = localEvent.event,
            expenses = expensesToAddFiltered,
            currencies = localEvent.currencies,
            persons = localEvent.persons
        )

        val networkExpenses = networkResults.mapNotNull { networkResult ->
            when (networkResult) {
                is IoResult.Success -> networkResult.data
                is IoResult.Error -> null
            }
        }

        withContext(NonCancellable) {
            networkExpenses.forEach {
                expensesLocalStore.updateExpenseServerId(it.expenseId, it.serverId)
            }
        }

        IoResult.Success(Unit)
    }
}