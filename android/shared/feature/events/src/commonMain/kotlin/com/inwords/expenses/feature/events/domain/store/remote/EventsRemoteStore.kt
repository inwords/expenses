package com.inwords.expenses.feature.events.domain.store.remote

import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person

internal interface EventsRemoteStore {

    sealed interface GetEventResult {
        data class Event(val event: EventDetails) : GetEventResult
        data object InvalidAccessCode : GetEventResult
        data object EventNotFound : GetEventResult
        data object OtherError : GetEventResult
    }

    suspend fun getEvent(
        event: Event,
        currencies: List<Currency>,
        localPersons: List<Person>?,
    ): GetEventResult

    suspend fun createEvent(
        event: Event,
        currencies: List<Currency>,
        primaryCurrencyServerId: String,
        localPersons: List<Person>,
    ): IoResult<EventDetails>

    suspend fun addPersonsToEvent(
        eventServerId: String,
        pinCode: String,
        localPersons: List<Person>
    ): IoResult<List<Person>>

}