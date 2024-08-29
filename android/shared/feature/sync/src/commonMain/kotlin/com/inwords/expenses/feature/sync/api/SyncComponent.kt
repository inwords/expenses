package com.inwords.expenses.feature.sync.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.sync.data.EventsSyncManagerFactory
import com.inwords.expenses.feature.sync.domain.EventsSyncObserver

class SyncComponent internal constructor(
    private val eventsSyncManagerFactory: EventsSyncManagerFactory,
    private val deps: SyncComponentFactory.Deps
) : Component {

    val eventsSyncObserver: EventsSyncObserver by lazy {
        EventsSyncObserver(
            eventsInteractor = lazy { deps.eventsInteractor },
            eventsSyncManagerFactory = eventsSyncManagerFactory
        )
    }
}