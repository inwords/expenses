package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import kotlinx.coroutines.withContext


class EventPullCurrenciesAndPersonsTask internal constructor(
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
     */
    suspend fun pullEventCurrenciesAndPersons(eventId: Long): IoResult<*> = withContext(IO) {
        val localEvent = eventsLocalStore.getEventWithDetails(eventId)
            ?.takeIf { it.event.serverId != 0L }
            ?: return@withContext IoResult.Error.Failure

        val remoteResult = eventsRemoteStore.getEvent(
            event = localEvent.event,
            currencies = localEvent.currencies,
            localPersons = localEvent.persons
        )

        val remoteEventDetails = when (remoteResult) {
            is EventsRemoteStore.GetEventResult.Event -> remoteResult.event

            EventsRemoteStore.GetEventResult.EventNotFound,
            EventsRemoteStore.GetEventResult.InvalidAccessCode -> return@withContext IoResult.Error.Failure

            EventsRemoteStore.GetEventResult.OtherError -> return@withContext IoResult.Error.Retry
        }

        transactionHelper.immediateWriteTransaction {
            currenciesPullTask.updateLocalCurrencies(remoteEventDetails.currencies, inTransaction = false)
            updateLocalEventPersons(
                eventId = localEvent.event.id,
                localPersons = localEvent.persons,
                remotePersons = remoteEventDetails.persons
            )
        }

        IoResult.Success(Unit)
    }

    private suspend fun updateLocalEventPersons(
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