package com.inwords.expenses.feature.sync.api

import android.content.Context
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.sync.data.EventsSyncManagerFactory

actual class SyncComponentFactory(private val deps: Deps) {

    actual interface Deps {
        val context: Context

        actual val eventsInteractorLazy: Lazy<EventsInteractor>
        actual val expensesInteractorLazy: Lazy<ExpensesInteractor>
    }

    actual fun create(): SyncComponent {
        val syncManagerFactory = EventsSyncManagerFactory(deps.context)
        return SyncComponent(eventsSyncManagerFactory = syncManagerFactory, deps = deps)
    }

}