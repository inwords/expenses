package com.inwords.expenses.feature.events.domain.store.remote

import com.inwords.expenses.core.utils.Result
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
        primaryCurrencyId: Long,
        localPersons: List<Person>,
    ): Result<EventDetails>

    suspend fun addPersonsToEvent(
        eventServerId: Long,
        pinCode: String,
        localPersons: List<Person>
    ): Result<List<Person>>

}