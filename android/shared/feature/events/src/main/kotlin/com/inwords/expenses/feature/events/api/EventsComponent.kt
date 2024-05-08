package com.inwords.expenses.feature.events.api

import com.inwords.expenses.feature.events.domain.EventsInteractor

class EventsComponent(private val deps: Deps) {

    interface Deps {

    }

    val eventsInteractor: EventsInteractor by lazy {
        EventsInteractor()
    }
}