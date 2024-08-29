package com.inwords.expenses.feature.sync.data

import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal actual class EventsSyncManager {

    @OptIn(DelicateCoroutinesApi::class)
    private val scope = GlobalScope + IO

    private val eventsComponent: EventsComponent
        get() = ComponentsMap.getComponent<EventsComponent>()

    private val expensesComponent: ExpensesComponent
        get() = ComponentsMap.getComponent<ExpensesComponent>()

    private val mutex = Mutex()

    actual fun pushAllEventInfo(eventId: Long) {
        scope.launch {
            mutex.withLock {
                eventsComponent.currenciesPullTask.value.pullCurrencies() is IoResult.Success &&
                    eventsComponent.eventPushTask.value.pushEvent(eventId) is IoResult.Success &&
                    (eventsComponent.eventPersonsPushTask.value.pushEventPersons(eventId) is IoResult.Success ||
                        eventsComponent.eventPullCurrenciesAndPersonsTask.value.pullEventCurrenciesAndPersons(eventId) is IoResult.Success) &&
                    (expensesComponent.eventExpensesPushTask.value.pushEventExpenses(eventId) is IoResult.Success ||
                        expensesComponent.eventExpensesPullTask.value.pullEventExpenses(eventId) is IoResult.Success)
            }
        }
    }

}

internal actual class EventsSyncManagerFactory {

    actual fun create(): EventsSyncManager {
        return EventsSyncManager()
    }
}