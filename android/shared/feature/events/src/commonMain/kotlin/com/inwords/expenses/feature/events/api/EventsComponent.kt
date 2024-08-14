package com.inwords.expenses.feature.events.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.data.EventsSyncManagerFactory
import com.inwords.expenses.feature.events.data.db.store.CurrenciesLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.EventsLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.PersonsLocalStoreImpl
import com.inwords.expenses.feature.events.data.network.CurrenciesRemoteStoreImpl
import com.inwords.expenses.feature.events.data.network.EventsRemoteStoreImpl
import com.inwords.expenses.feature.events.domain.CurrenciesPullTask
import com.inwords.expenses.feature.events.domain.EventPersonsPushTask
import com.inwords.expenses.feature.events.domain.EventPushTask
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsSyncObserver
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore

class EventsComponent internal constructor(
    private val eventsSyncManagerFactory: EventsSyncManagerFactory,
    private val deps: EventsComponentFactory.Deps
) : Component {

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
            currenciesLocalStore = currenciesLocalStore.value,
        )
    }

    val eventsInteractor: EventsInteractor by lazy {
        EventsInteractor(
            eventsLocalStore = eventsLocalStore.value,
            eventsRemoteStore = eventsRemoteStore.value,
            settingsRepository = deps.settingsRepository,
            currenciesPullTask = currenciesPullTask,
        )
    }

    internal val currenciesPullTask: CurrenciesPullTask by lazy {
        CurrenciesPullTask(
            currenciesLocalStore = currenciesLocalStore.value,
            currenciesRemoteStore = currenciesRemoteStore.value
        )
    }

    internal val eventPushTask: EventPushTask by lazy {
        EventPushTask(
            eventsLocalStore = eventsLocalStore.value,
            eventsRemoteStore = eventsRemoteStore.value,
            personsLocalStore = personsLocalStore.value,
        )
    }

    internal val eventPersonsPushTask: EventPersonsPushTask by lazy {
        EventPersonsPushTask(
            eventsLocalStore = eventsLocalStore.value,
            eventsRemoteStore = eventsRemoteStore.value,
            personsLocalStore = personsLocalStore.value,
        )
    }

    val eventsSyncObserver: EventsSyncObserver by lazy {
        EventsSyncObserver(
            eventsInteractor = lazy { eventsInteractor },
            eventsSyncManagerFactory = eventsSyncManagerFactory
        )
    }

}