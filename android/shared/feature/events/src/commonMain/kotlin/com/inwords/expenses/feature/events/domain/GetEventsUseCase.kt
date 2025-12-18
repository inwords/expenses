package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import kotlinx.coroutines.flow.Flow

class GetEventsUseCase internal constructor(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
) {
    private val eventsLocalStore by eventsLocalStoreLazy

    fun getEvents(): Flow<List<Event>> {
        return eventsLocalStore.getEventsFlow()
    }
}

