package com.inwords.expenses.feature.events.api

import androidx.room.RoomDatabase
import com.inwords.expenses.feature.events.data.CurrenciesRepositoryImpl
import com.inwords.expenses.feature.events.data.EventsRepositoryImpl
import com.inwords.expenses.feature.events.data.PersonsRepositoryImpl
import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository

class EventsComponent(private val deps: Deps) : Component {

    interface Deps {

        val eventsDao: EventsDao
        val personsDao: PersonsDao
        val currenciesDao: CurrenciesDao

        val db: RoomDatabase

        val settingsRepository: SettingsRepository
    }

    val eventsInteractor: EventsInteractor by lazy {
        EventsInteractor(
            eventsRepository = EventsRepositoryImpl(
                dbLazy = lazy { deps.db },
                eventsDaoLazy = lazy { deps.eventsDao },
                personsRepositoryLazy = lazy { PersonsRepositoryImpl(lazy { deps.personsDao }) },
                currenciesRepositoryLazy = lazy { CurrenciesRepositoryImpl(lazy { deps.currenciesDao }) },
            ),
            settingsRepository = deps.settingsRepository
        )
    }
}