package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class EventPersonsPushTask internal constructor(
    private val eventsLocalStore: EventsLocalStore,
    private val eventsRemoteStore: EventsRemoteStore,
    private val personsLocalStore: PersonsLocalStore,
) {

    /**
     * Prerequisites:
     * 1. Event is synced
     */
    internal suspend fun pushEventPersons(eventId: Long): Boolean = withContext(IO) {
        val localEvent = eventsLocalStore.getEventWithDetails(eventId).first() ?: return@withContext false

        val personsToAdd = localEvent.persons.filter { it.serverId == 0L }

        if (personsToAdd.isEmpty()) return@withContext true

        val networkResult = eventsRemoteStore.addPersonsToEvent(
            eventServerId = localEvent.event.serverId,
            pinCode = localEvent.event.pinCode,
            localPersons = personsToAdd
        )

        val networkPersons = when (networkResult) {
            is Result.Success -> networkResult.data
            is Result.Error -> return@withContext false
        }

        personsLocalStore.insert(networkPersons)

        true
    }
}