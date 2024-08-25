package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.withContext


internal class EventPullCurrenciesAndPersonsTask(
    transactionHelperLazy: Lazy<TransactionHelper>,
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
    currenciesPullTaskLazy: Lazy<CurrenciesPullTask>
) {

    private val transactionHelper by transactionHelperLazy
    private val eventsLocalStore by eventsLocalStoreLazy
    private val eventsRemoteStore by eventsRemoteStoreLazy
    private val currenciesPullTask by currenciesPullTaskLazy

    /**
     * Prerequisites:
     * 1. Event has serverId
     *
     * @return true if event was found and updated, false is error is recoverable, null if error is not recoverable
     */
    suspend fun pullEventCurrenciesAndPersons(eventId: Long): Boolean? = withContext(IO) {
        val localEvent = eventsLocalStore.getEventWithDetails(eventId)?.takeIf { it.event.serverId != 0L } ?: return@withContext null

        val remoteResult = eventsRemoteStore.getEvent(
            event = localEvent.event,
            currencies = localEvent.currencies,
            localPersons = localEvent.persons
        )

        val remoteEventDetails = when (remoteResult) {
            is EventsRemoteStore.GetEventResult.Event -> remoteResult.event

            EventsRemoteStore.GetEventResult.EventNotFound,
            EventsRemoteStore.GetEventResult.InvalidAccessCode -> return@withContext null

            EventsRemoteStore.GetEventResult.OtherError -> return@withContext false
        }

        transactionHelper.immediateWriteTransaction {
            currenciesPullTask.updateLocalCurrencies(remoteEventDetails.currencies, inTransaction = false)
            updateLocalEventPersonsInternal(
                eventId = localEvent.event.id,
                localPersons = localEvent.persons,
                remotePersons = remoteEventDetails.persons
            )
        }

        true
    }

    private suspend fun updateLocalEventPersonsInternal(
        eventId: Long,
        localPersons: List<Person>,
        remotePersons: List<Person>
    ): List<Person> {
        val localPersonsMap = localPersons.mapTo(HashSet()) { it.serverId }

        val personsToInsert = remotePersons.filter { remotePerson ->
            remotePerson.serverId !in localPersonsMap
        }

        return if (personsToInsert.isNotEmpty()) {
            eventsLocalStore.insertPersonsWithCrossRefs(eventId, personsToInsert, inTransaction = false)
        } else {
            localPersons
        }
    }
}