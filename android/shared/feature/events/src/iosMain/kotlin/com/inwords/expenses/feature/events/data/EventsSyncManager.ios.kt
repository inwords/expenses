package com.inwords.expenses.feature.events.data

import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.api.EventsComponent
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

    private val mutex = Mutex()

    actual fun pushAllEventInfo(eventId: Long) {
        scope.launch {
            mutex.withLock {
                eventsComponent.currenciesPullTask.pullCurrencies() &&
                    eventsComponent.eventPushTask.pushEvent(eventId) &&
                    eventsComponent.eventPersonsPushTask.pushEventPersons(eventId)
            }
        }
    }

}

internal actual class EventsSyncManagerFactory {

    actual fun create(): EventsSyncManager {
        return EventsSyncManager()
    }
}