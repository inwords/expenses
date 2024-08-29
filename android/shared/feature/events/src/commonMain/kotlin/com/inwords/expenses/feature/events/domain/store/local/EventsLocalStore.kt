package com.inwords.expenses.feature.events.domain.store.local

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface EventsLocalStore {

    fun getEventsFlow(): Flow<List<Event>>
    fun getEventWithDetailsFlow(eventId: Long): Flow<EventDetails?>

    suspend fun getEvent(eventId: Long): Event?
    suspend fun getEventWithDetails(eventId: Long): EventDetails?

    suspend fun getEventWithDetailsByServerId(eventServerId: Long): EventDetails?

    suspend fun getEventPersons(eventId: Long): List<Person>

    suspend fun update(eventId: Long, newServerId: Long): Boolean

    suspend fun deepInsert(
        eventToInsert: Event,
        personsToInsert: List<Person>,
        primaryCurrencyId: Long,
        prefetchedLocalCurrencies: List<Currency>? = null,
        inTransaction: Boolean
    ): EventDetails

    suspend fun insertPersonsWithCrossRefs(
        eventId: Long,
        persons: List<Person>,
        inTransaction: Boolean,
    ): List<Person>

}