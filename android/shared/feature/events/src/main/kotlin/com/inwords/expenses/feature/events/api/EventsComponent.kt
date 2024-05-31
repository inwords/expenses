package com.inwords.expenses.feature.events.api

import com.inwords.expenses.feature.events.data.CurrenciesRepositoryImpl
import com.inwords.expenses.feature.events.data.EventsRepositoryImpl
import com.inwords.expenses.feature.events.data.PersonsRepositoryImpl
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.EventsInteractor

class EventsComponent(private val deps: Deps) {

    interface Deps {

        val eventsDao: EventsDao
        val personsDao: PersonsDao
        val currenciesDao: CurrenciesDao
    }

    val eventsInteractor: EventsInteractor by lazy {
        EventsInteractor(
            eventsRepository = EventsRepositoryImpl(lazy { deps.eventsDao }),
            personsRepository = PersonsRepositoryImpl(lazy { deps.personsDao }),
            currenciesRepository = CurrenciesRepositoryImpl(lazy { deps.currenciesDao })
        )
    }
}