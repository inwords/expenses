package com.inwords.expenses.feature.events.domain.store.remote

import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person

internal interface EventsRemoteStore {

    sealed interface EventNetworkError {
        data object InvalidAccessCode : EventNetworkError
        data object NotFound : EventNetworkError
        data object Gone : EventNetworkError
        data object OtherError : EventNetworkError
    }

    sealed interface GetEventResult {
        data class Event(val event: EventDetails) : GetEventResult
        data class Error(val error: EventNetworkError) : GetEventResult
    }

    sealed interface DeleteEventResult {
        data object Deleted : DeleteEventResult
        data class Error(val error: EventNetworkError) : DeleteEventResult
    }

    suspend fun getEvent(
        localId: Long,
        serverId: String,
        pinCode: String,
        currencies: List<Currency>,
        localPersons: List<Person>?,
    ): GetEventResult

    suspend fun createEvent(
        event: Event,
        currencies: List<Currency>,
        primaryCurrencyServerId: String,
        localPersons: List<Person>,
    ): IoResult<EventDetails>

    suspend fun deleteEvent(serverId: String, pinCode: String): DeleteEventResult

    suspend fun addPersonsToEvent(
        eventServerId: String,
        pinCode: String,
        localPersons: List<Person>
    ): IoResult<List<Person>>

}