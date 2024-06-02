package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.flow.Flow

internal interface EventsRepository {

    fun getEvents(): Flow<List<Event>>
    fun getEvent(eventId: Long): Flow<Event>
    fun getEventWithDetails(eventId: Long): Flow<EventDetails>

    suspend fun deepInsert(
        eventToInsert: Event,
        personsToInsert: List<Person>,
        currenciesToInsert: List<Currency>,
        primaryPersonIndex: Int,
        primaryCurrencyIndex: Int
    ): EventDetails
}