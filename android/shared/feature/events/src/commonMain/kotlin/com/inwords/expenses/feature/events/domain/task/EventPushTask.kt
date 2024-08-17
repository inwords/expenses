package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

internal class EventPushTask(
    transactionHelperLazy: Lazy<TransactionHelper>,
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
    personsLocalStoreLazy: Lazy<PersonsLocalStore>,
) {

    private val transactionHelper by transactionHelperLazy
    private val eventsLocalStore by eventsLocalStoreLazy
    private val eventsRemoteStore by eventsRemoteStoreLazy
    private val personsLocalStore by personsLocalStoreLazy

    /**
     * Prerequisites:
     * 1. Currencies are synced
     */
    suspend fun pushEvent(eventId: Long): Boolean = withContext(IO) {
        val localEventDetails = eventsLocalStore.getEventWithDetails(eventId).first() ?: return@withContext false

        if (localEventDetails.event.serverId != 0L) return@withContext true

        val remoteEventDetailsResult = eventsRemoteStore.createEvent(
            event = localEventDetails.event,
            currencies = localEventDetails.currencies,
            primaryCurrencyId = localEventDetails.primaryCurrency.serverId,
            localPersons = localEventDetails.persons
        )
        val networkEventDetails = when (remoteEventDetailsResult) {
            is Result.Success -> remoteEventDetailsResult.data
            is Result.Error -> return@withContext false
        }

        val localPersons = localEventDetails.persons
        val updatedPersons = networkEventDetails.persons.mapIndexed { i, networkPerson ->
            localPersons[i].copy(serverId = networkPerson.serverId)
        }

        val updatedEvent = localEventDetails.copy(
            event = localEventDetails.event.copy(serverId = networkEventDetails.event.serverId),
            persons = updatedPersons
        )

        transactionHelper.immediateWriteTransaction {
            personsLocalStore.insertWithoutCrossRefs(updatedPersons)

            eventsLocalStore.update(eventId, updatedEvent.event.serverId)
        }

        true
    }
}