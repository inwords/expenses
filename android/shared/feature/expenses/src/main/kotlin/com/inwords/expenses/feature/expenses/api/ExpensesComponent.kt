package com.inwords.expenses.feature.expenses.api

import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.data.ExpensesRepositoryImpl
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor

class ExpensesComponent(private val deps: Deps) {

    interface Deps {

        val eventsInteractor: EventsInteractor

        val expensesDao: ExpensesDao
    }

    val expensesInteractor: ExpensesInteractor by lazy {
        val repository = ExpensesRepositoryImpl(lazy { deps.expensesDao })
        ExpensesInteractor(deps.eventsInteractor, repository)
    }
}