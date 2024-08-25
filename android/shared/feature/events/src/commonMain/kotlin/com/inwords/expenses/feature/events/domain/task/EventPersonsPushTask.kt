package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.withContext


internal class EventPersonsPushTask(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
) {

    private val eventsLocalStore by eventsLocalStoreLazy
    private val eventsRemoteStore by eventsRemoteStoreLazy

    /**
     * Prerequisites:
     * 1. Event is synced
     */
    suspend fun pushEventPersons(eventId: Long): Boolean = withContext(IO) {
        val localEvent = eventsLocalStore.getEventWithDetails(eventId) ?: return@withContext false

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

        eventsLocalStore.insertPersonsWithCrossRefs(eventId, networkPersons, inTransaction = true)

        true
    }
}