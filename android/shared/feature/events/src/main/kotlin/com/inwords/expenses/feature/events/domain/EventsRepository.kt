package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import kotlinx.coroutines.flow.Flow

internal interface EventsRepository {

    fun getEvents(): Flow<List<Event>>
    fun getEventWithDetails(eventId: Long): Flow<EventDetails>

    suspend fun insert(event: EventDetails): EventDetails
}