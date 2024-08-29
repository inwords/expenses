package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.withContext

class EventPushTask internal constructor(
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
    suspend fun pushEvent(eventId: Long): IoResult<*> = withContext(IO) {
        val localEventDetails = eventsLocalStore.getEventWithDetails(eventId) ?: return@withContext IoResult.Error.Failure

        if (localEventDetails.event.serverId != 0L) return@withContext IoResult.Success(Unit)

        val remoteEventDetailsResult = eventsRemoteStore.createEvent(
            event = localEventDetails.event,
            currencies = localEventDetails.currencies,
            primaryCurrencyId = localEventDetails.primaryCurrency.serverId,
            localPersons = localEventDetails.persons
        )
        val networkEventDetails = when (remoteEventDetailsResult) {
            is IoResult.Success -> remoteEventDetailsResult.data
            is IoResult.Error -> return@withContext remoteEventDetailsResult
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

        IoResult.Success(Unit)
    }
}