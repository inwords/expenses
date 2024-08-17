package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.events.domain.task.CurrenciesPullTask
import kotlinx.coroutines.flow.first

internal class JoinRemoteEventUseCase(
    transactionHelperLazy: Lazy<TransactionHelper>,
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
    currenciesPullTaskLazy: Lazy<CurrenciesPullTask>,
) {

    private val transactionHelper by transactionHelperLazy
    private val eventsLocalStore by eventsLocalStoreLazy
    private val eventsRemoteStore by eventsRemoteStoreLazy
    private val currenciesLocalStore by currenciesLocalStoreLazy
    private val currenciesPullTask by currenciesPullTaskLazy

    suspend fun joinRemoteEvent(
        event: Event,
        localCurrencies: List<Currency>?,
        localPersons: List<Person>?,
    ): JoinEventResult {
        val currenciesNotNull = localCurrencies ?: currenciesLocalStore.getCurrencies().first()

        val remoteEvent = when (val eventResult = eventsRemoteStore.getEvent(event, currenciesNotNull, localPersons)) {
            is EventsRemoteStore.GetEventResult.Event -> eventResult.event

            EventsRemoteStore.GetEventResult.EventNotFound -> return JoinEventResult.EventNotFound
            EventsRemoteStore.GetEventResult.InvalidAccessCode -> return JoinEventResult.InvalidAccessCode
            EventsRemoteStore.GetEventResult.OtherError -> return JoinEventResult.OtherError
        }

        val eventDetails = transactionHelper.immediateWriteTransaction {
            val updatedCurrencies = currenciesPullTask.updateLocalCurrencies(remoteEvent.currencies, inTransaction = false)

            eventsLocalStore.deepInsert(
                eventToInsert = remoteEvent.event,
                personsToInsert = remoteEvent.persons,
                primaryCurrencyId = updatedCurrencies.first { it.serverId == remoteEvent.primaryCurrency.serverId }.id,
                prefetchedLocalCurrencies = updatedCurrencies,
                inTransaction = false,
            )
        }

        return JoinEventResult.NewCurrentEvent(eventDetails)
    }

}