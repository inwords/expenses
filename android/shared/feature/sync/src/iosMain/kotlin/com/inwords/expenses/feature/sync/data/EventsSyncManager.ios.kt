package com.inwords.expenses.feature.sync.data

import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

actual class EventsSyncManager internal constructor() {

    @OptIn(DelicateCoroutinesApi::class)
    private val scope = GlobalScope + IO

    private val eventsComponent: EventsComponent
        get() = ComponentsMap.getComponent<EventsComponent>()

    private val expensesComponent: ExpensesComponent
        get() = ComponentsMap.getComponent<ExpensesComponent>()

    private val lock = ReentrantLock()

    private val jobs = hashMapOf<Long, Job>()

    internal actual fun pushAllEventInfo(eventId: Long) {
        scope.launch {
            lock.withLock {
                if (jobs[eventId]?.isActive == true) return@launch

                val newJob = launch {
                    val currenciesResult = eventsComponent.currenciesPullTask.value.pullCurrencies()
                    if (currenciesResult !is IoResult.Success) return@launch

                    val eventPushResult = eventsComponent.eventPushTask.value.pushEvent(eventId)
                    if (eventPushResult !is IoResult.Success) return@launch

                    val personsPushResult = eventsComponent.eventPersonsPushTask.value.pushEventPersons(eventId)
                    if (personsPushResult !is IoResult.Success) return@launch

                    val personsPullResult = eventsComponent.eventPullPersonsTask.value.pullEventPersons(eventId)
                    if (personsPullResult !is IoResult.Success) return@launch

                    val expensesPushResult = expensesComponent.eventExpensesPushTask.value.pushEventExpenses(eventId)
                    if (expensesPushResult !is IoResult.Success) return@launch

                    val expensesPullResult = expensesComponent.eventExpensesPullTask.value.pullEventExpenses(eventId)
                    if (expensesPullResult !is IoResult.Success) return@launch
                }

                jobs[eventId] = newJob

                // Clean up completed jobs
                newJob.invokeOnCompletion {
                    lock.withLock {
                        if (jobs[eventId] == newJob) {
                            jobs.remove(eventId)
                        }
                    }
                }
            }
        }
    }

    actual suspend fun cancelEventSync(eventId: Long) {
        lock.withLock {
            jobs.remove(eventId)
        }?.cancelAndJoin()
    }

}

internal actual class EventsSyncManagerFactory {

    actual fun create(): EventsSyncManager {
        return EventsSyncManager()
    }
}
