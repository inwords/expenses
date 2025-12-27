package com.inwords.expenses.feature.events.api

import kotlinx.coroutines.flow.StateFlow

interface EventDeletionStateManager {

    sealed interface EventDeletionState {
        data object None : EventDeletionState
        data object PendingDeletionChoice : EventDeletionState
        data object Loading : EventDeletionState
        data object RemoteDeletionFailed : EventDeletionState
    }

    val eventsDeletionState: StateFlow<Map<Long, EventDeletionState>>

    fun setEventDeletionState(eventId: Long, state: EventDeletionState)
    fun clearEventDeletionState(eventId: Long)
}
