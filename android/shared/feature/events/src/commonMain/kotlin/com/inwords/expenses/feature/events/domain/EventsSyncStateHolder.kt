package com.inwords.expenses.feature.events.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class EventsSyncStateHolder internal constructor() {

    private val _state = MutableStateFlow<Set<Long>>(emptySet())
    val state: StateFlow<Set<Long>> = _state

    fun setSyncState(syncingEvents: Set<Long>) {
        _state.value = syncingEvents
    }

    fun getStateFor(eventId: Long): Flow<Boolean> {
        return state
            .map { it.contains(eventId) }
            .distinctUntilChanged()
    }

}
