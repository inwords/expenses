package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.feature.events.data.EventsSyncManagerFactory
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

class EventsSyncObserver internal constructor(
    eventsInteractor: Lazy<EventsInteractor>,
    private val eventsSyncManagerFactory: EventsSyncManagerFactory
) {
    private val eventsInteractor by eventsInteractor
    private val eventsSyncManager by lazy { eventsSyncManagerFactory.create() }

    /**
     * Should be called only once per process lifecycle
     */
    fun observeNewEventsIn(scope: CoroutineScope) {
        eventsInteractor.currentEvent
            .filterNotNull()
            .distinctUntilChanged { old, new ->
                old.event.id == new.event.id &&
                    old.persons.toIdsSet() == new.persons.toIdsSet()
            }
            .collectIn(scope) { currentEvent ->
                eventsSyncManager.pushAllEventInfo(currentEvent.event.id)
            }
    }

    private fun List<Person>.toIdsSet(): Set<Long> {
        return mapTo(HashSet(size)) { it.id }
    }

}