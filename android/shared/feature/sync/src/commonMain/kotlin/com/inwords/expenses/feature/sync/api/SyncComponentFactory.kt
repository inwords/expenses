package com.inwords.expenses.feature.sync.api

import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor

expect class SyncComponentFactory {

    interface Deps {

        val eventsInteractor: EventsInteractor
        val expensesInteractor: ExpensesInteractor

    }

    fun create(): SyncComponent
}