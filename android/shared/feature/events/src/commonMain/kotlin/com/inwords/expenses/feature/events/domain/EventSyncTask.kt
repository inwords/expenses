package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class EventSyncTask internal constructor(
    private val eventsLocalStore: EventsLocalStore,
    private val eventsRemoteStore: EventsRemoteStore,
    private val personsLocalStore: PersonsLocalStore,
) {

    /**
     * Prerequisites:
     * 1. Currencies are synced
     */
    suspend fun syncEvent(eventId: Long): Boolean = withContext(IO) {
        val localEventDetails = eventsLocalStore.getEventWithDetails(eventId).first()

        if (localEventDetails.event.serverId != 0L) return@withContext true

        val remoteEventDetailsResult = eventsRemoteStore.createEvent(
            name = localEventDetails.event.name,
            pinCode = localEventDetails.event.pinCode,
            currencies = localEventDetails.currencies,
            primaryCurrencyId = localEventDetails.primaryCurrency.serverId,
            users = localEventDetails.persons
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

        // FIXME transaction
        withContext(NonCancellable) {
            personsLocalStore.insert(updatedPersons)

            eventsLocalStore.update(eventId, updatedEvent.event.serverId)
        }

        true
    }
}