package com.inwords.expenses.feature.events.api

import androidx.room.RoomDatabase
import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.data.db.store.CurrenciesLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.EventsLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.PersonsLocalStoreImpl
import com.inwords.expenses.feature.events.data.network.CurrenciesRemoteStoreImpl
import com.inwords.expenses.feature.events.data.network.EventsRemoteStoreImpl
import com.inwords.expenses.feature.events.domain.CurrenciesSyncTask
import com.inwords.expenses.feature.events.domain.EventSyncTask
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import io.ktor.client.HttpClient

class EventsComponent(private val deps: Deps) : Component {

    interface Deps {

        val eventsDao: EventsDao
        val personsDao: PersonsDao
        val currenciesDao: CurrenciesDao

        val db: RoomDatabase

        val client: SuspendLazy<HttpClient>
        val hostConfig: HostConfig

        val settingsRepository: SettingsRepository
    }

    private val currenciesLocalStore: Lazy<CurrenciesLocalStore> = lazy {
        CurrenciesLocalStoreImpl(currenciesDaoLazy = lazy { deps.currenciesDao })
    }

    private val currenciesRemoteStore: Lazy<CurrenciesRemoteStore> = lazy {
        CurrenciesRemoteStoreImpl(
            client = deps.client,
            hostConfig = deps.hostConfig
        )
    }

    private val personsLocalStore: Lazy<PersonsLocalStore> = lazy {
        PersonsLocalStoreImpl(lazy { deps.personsDao })
    }

    private val eventsLocalStore: Lazy<EventsLocalStore> = lazy {
        EventsLocalStoreImpl(
            dbLazy = lazy { deps.db },
            eventsDaoLazy = lazy { deps.eventsDao },
            personsLocalStoreLazy = personsLocalStore,
            currenciesLocalStoreLazy = currenciesLocalStore,
        )
    }

    private val eventsRemoteStore: Lazy<EventsRemoteStore> = lazy {
        EventsRemoteStoreImpl(
            client = deps.client,
            hostConfig = deps.hostConfig,
            currenciesRemoteStore = currenciesRemoteStore.value,
        )
    }

    val eventsInteractor: EventsInteractor by lazy {
        EventsInteractor(
            eventsLocalStore = eventsLocalStore.value,
            settingsRepository = deps.settingsRepository
        )
    }


    val eventSyncTask: EventSyncTask by lazy {
        EventSyncTask(
            eventsLocalStore = eventsLocalStore.value,
            eventsRemoteStore = eventsRemoteStore.value,
            personsLocalStore = personsLocalStore.value,
        )
    }

    val currenciesSyncTask: CurrenciesSyncTask by lazy {
        CurrenciesSyncTask(
            currenciesLocalStore = currenciesLocalStore.value,
            currenciesRemoteStore = currenciesRemoteStore.value
        )
    }

}