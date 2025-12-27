package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.api.EventDeletionStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class EventDeletionStateManagerImpl : EventDeletionStateManager {
    private val _eventsFailedToDeleteRemotely = MutableStateFlow<Map<Long, EventDeletionStateManager.EventDeletionState>>(emptyMap())
    override val eventsDeletionState: StateFlow<Map<Long, EventDeletionStateManager.EventDeletionState>> = _eventsFailedToDeleteRemotely

    override fun setEventDeletionState(eventId: Long, state: EventDeletionStateManager.EventDeletionState) {
        _eventsFailedToDeleteRemotely.update { it + (eventId to state) }
    }

    override fun clearEventDeletionState(eventId: Long) {
        _eventsFailedToDeleteRemotely.update { it - eventId }
    }
}
