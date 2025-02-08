package com.inwords.expenses.feature.menu.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.domain.EventsInteractor

class MenuComponent(private val deps: Deps) : Component {

    interface Deps {

        val eventsInteractor: EventsInteractor
    }


}