package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.api.EventDeletionStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class EventDeletionStateManagerImpl : EventDeletionStateManager {

    override val eventsDeletionState = MutableStateFlow<Map<Long, EventDeletionStateManager.EventDeletionState>>(emptyMap())

    override fun setEventDeletionState(eventId: Long, state: EventDeletionStateManager.EventDeletionState) {
        eventsDeletionState.update { it + (eventId to state) }
    }

    override fun clearEventDeletionState(eventId: Long) {
        eventsDeletionState.update { it - eventId }
    }
}
