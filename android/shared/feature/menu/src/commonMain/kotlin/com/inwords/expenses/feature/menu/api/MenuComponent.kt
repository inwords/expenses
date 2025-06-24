package com.inwords.expenses.feature.menu.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.share.api.ShareManager

class MenuComponent(private val deps: Deps) : Component {

    interface Deps {

        val eventsInteractor: EventsInteractor
        val shareManager: ShareManager
    }


}