package com.inwords.expenses.feature.sync.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.sync.data.EventsSyncManager
import com.inwords.expenses.feature.sync.data.EventsSyncManagerFactory
import com.inwords.expenses.feature.sync.domain.EventsSyncObserver

class SyncComponent internal constructor(
    private val eventsSyncManagerFactory: EventsSyncManagerFactory,
    private val deps: SyncComponentFactory.Deps
) : Component {

    private val eventsSyncManagerLazy = lazy {
        eventsSyncManagerFactory.create()
    }

    val eventsSyncObserver: EventsSyncObserver by lazy {
        EventsSyncObserver(
            eventsInteractorLazy = lazy { deps.eventsInteractor },
            expensesInteractorLazy = lazy { deps.expensesInteractor },
            eventsSyncManagerLazy = eventsSyncManagerLazy
        )
    }

    val eventsSyncManager: EventsSyncManager by eventsSyncManagerLazy

}