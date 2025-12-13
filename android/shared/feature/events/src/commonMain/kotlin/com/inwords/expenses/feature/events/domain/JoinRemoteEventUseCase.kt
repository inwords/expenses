package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore.EventNetworkError
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore.GetEventResult
import com.inwords.expenses.feature.events.domain.task.CurrenciesPullTask
import kotlinx.coroutines.flow.first

internal class JoinRemoteEventUseCase(
    transactionHelperLazy: Lazy<TransactionHelper>,
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
    deleteEventUseCase: Lazy<DeleteEventUseCase>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
    currenciesPullTaskLazy: Lazy<CurrenciesPullTask>,
) {

    private val transactionHelper by transactionHelperLazy
    private val eventsLocalStore by eventsLocalStoreLazy
    private val eventsRemoteStore by eventsRemoteStoreLazy
    private val deleteEventUseCase by deleteEventUseCase
    private val currenciesLocalStore by currenciesLocalStoreLazy
    private val currenciesPullTask by currenciesPullTaskLazy

    suspend fun joinRemoteEvent(
        serverId: String,
        pinCode: String,
    ): JoinEventResult {
        val currenciesBeforeSync = currenciesLocalStore.getCurrencies().first()
        val currencies = if (currenciesBeforeSync.isEmpty() || currenciesBeforeSync.any { it.serverId == null }) {
            when (val currencies = currenciesPullTask.pullCurrencies()) {
                is IoResult.Success<List<Currency>> -> currencies.data
                is IoResult.Error -> return JoinEventResult.Error.OtherError
            }
        } else {
            currenciesBeforeSync
        }

        val remoteEvent = when (val eventResult = eventsRemoteStore.getEvent(
            localId = 0L,
            serverId = serverId,
            pinCode = pinCode,
            currencies = currencies,
            localPersons = null
        )) {
            is GetEventResult.Event -> eventResult.event
            is GetEventResult.Error -> return when (eventResult.error) {
                EventNetworkError.InvalidAccessCode -> JoinEventResult.Error.InvalidAccessCode
                EventNetworkError.NotFound -> JoinEventResult.Error.EventNotFound
                EventNetworkError.Gone -> {
                    deleteEventUseCase.deleteLocalEventByServerId(serverId)
                    JoinEventResult.Error.EventNotFound
                }

                EventNetworkError.OtherError -> JoinEventResult.Error.OtherError
            }
        }

        val eventDetails = transactionHelper.immediateWriteTransaction {
            eventsLocalStore.deepInsert(
                eventToInsert = remoteEvent.event,
                personsToInsert = remoteEvent.persons,
                prefetchedLocalCurrencies = remoteEvent.currencies,
                inTransaction = false,
            )
        }

        return JoinEventResult.NewCurrentEvent(eventDetails)
    }

}