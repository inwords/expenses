package com.inwords.expenses.feature.sync.api

import com.inwords.expenses.feature.events.domain.EventsInteractor

expect class SyncComponentFactory {

    interface Deps {

        val eventsInteractor: EventsInteractor

    }

    fun create(): SyncComponent
}