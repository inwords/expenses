package com.inwords.expenses.feature.events.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class EventsSyncStateHolder internal constructor() {

    val state: StateFlow<Set<Long>>
        field = MutableStateFlow<Set<Long>>(emptySet())

    fun setSyncState(syncingEvents: Set<Long>) {
        state.value = syncingEvents
    }

    fun getStateFor(eventId: Long): Flow<Boolean> {
        return state
            .map { it.contains(eventId) }
            .distinctUntilChanged()
    }

}
