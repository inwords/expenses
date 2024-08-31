package com.inwords.expenses.feature.sync.domain

import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.sync.data.EventsSyncManagerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class EventsSyncObserver internal constructor(
    eventsInteractorLazy: Lazy<EventsInteractor>,
    expensesInteractorLazy: Lazy<ExpensesInteractor>,
    private val eventsSyncManagerFactory: EventsSyncManagerFactory
) {
    private val eventsInteractor by eventsInteractorLazy
    private val expensesInteractor by expensesInteractorLazy
    private val eventsSyncManager by lazy { eventsSyncManagerFactory.create() }

    /**
     * Should be called only once per process lifecycle
     */
    fun observeNewEventsIn(scope: CoroutineScope) {
        merge(
            eventsInteractor.currentEvent
                .filterNotNull()
                .distinctUntilChanged { old, new ->
                    old.event.id == new.event.id &&
                        old.persons.personsToIdsSet() == new.persons.personsToIdsSet()
                }
                .flatMapLatestNoBuffer { currentEvent ->
                    expensesInteractor.getExpensesFlow(currentEvent.event.id).map { currentEvent to it }
                }
                .distinctUntilChanged { old, new ->
                    old.second.expensesToIdsSet() == new.second.expensesToIdsSet()
                }
                .map { it.first.event },
            expensesInteractor.refreshExpenses
        )
            .conflate()
            .collectIn(scope) { event ->
                eventsSyncManager.pushAllEventInfo(event.id)
                delay(5000) // do not launch sync more often than every 5 seconds
            }
    }

    private fun List<Person>.personsToIdsSet(): Set<Long> {
        return mapTo(HashSet(size)) { it.id }
    }

    private fun List<Expense>.expensesToIdsSet(): Set<Long> {
        return mapTo(HashSet(size)) { it.expenseId }
    }

}