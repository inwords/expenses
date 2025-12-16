package com.inwords.expenses.feature.events.api

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.data.db.store.CurrenciesLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.EventsLocalStoreImpl
import com.inwords.expenses.feature.events.data.db.store.PersonsLocalStoreImpl
import com.inwords.expenses.feature.events.data.network.store.CurrenciesRemoteStoreImpl
import com.inwords.expenses.feature.events.data.network.store.EventsRemoteStoreImpl
import com.inwords.expenses.feature.events.domain.DeleteEventUseCase
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.JoinEventUseCase
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.events.domain.task.CurrenciesPullTask
import com.inwords.expenses.feature.events.domain.task.EventPersonsPushTask
import com.inwords.expenses.feature.events.domain.task.EventPullPersonsTask
import com.inwords.expenses.feature.events.domain.task.EventPushTask

class EventsComponent internal constructor(
    private val deps: EventsComponentFactory.Deps
) : Component {

    private val transactionHelper: Lazy<TransactionHelper> = lazy { deps.transactionHelper }

    val currenciesLocalStore: Lazy<CurrenciesLocalStore> = lazy {
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

    val eventsLocalStore: Lazy<EventsLocalStore> = lazy {
        EventsLocalStoreImpl(
            transactionHelperLazy = transactionHelper,
            eventsDaoLazy = lazy { deps.eventsDao },
            personsLocalStoreLazy = personsLocalStore,
            currenciesLocalStoreLazy = currenciesLocalStore,
        )
    }

    private val eventsRemoteStore: Lazy<EventsRemoteStore> = lazy {
        EventsRemoteStoreImpl(
            client = deps.client,
            hostConfig = deps.hostConfig,
        )
    }

    val currenciesPullTask: Lazy<CurrenciesPullTask> = lazy {
        CurrenciesPullTask(
            transactionHelperLazy = transactionHelper,
            currenciesLocalStoreLazy = currenciesLocalStore,
            currenciesRemoteStoreLazy = currenciesRemoteStore
        )
    }

    val eventPushTask: Lazy<EventPushTask> = lazy {
        EventPushTask(
            transactionHelperLazy = transactionHelper,
            eventsLocalStoreLazy = eventsLocalStore,
            eventsRemoteStoreLazy = eventsRemoteStore,
            personsLocalStoreLazy = personsLocalStore,
        )
    }

    val eventPersonsPushTask: Lazy<EventPersonsPushTask> = lazy {
        EventPersonsPushTask(
            eventsLocalStoreLazy = eventsLocalStore,
            eventsRemoteStoreLazy = eventsRemoteStore,
        )
    }

    val eventPullPersonsTask: Lazy<EventPullPersonsTask> = lazy {
        EventPullPersonsTask(
            transactionHelperLazy = transactionHelper,
            eventsLocalStoreLazy = eventsLocalStore,
            eventsRemoteStoreLazy = eventsRemoteStore,
        )
    }

    val deleteEventUseCaseLazy: Lazy<DeleteEventUseCase> = lazy {
        DeleteEventUseCase(
            eventsLocalStoreLazy = eventsLocalStore,
            eventsRemoteStoreLazy = eventsRemoteStore,
            settingsRepositoryLazy = deps.settingsRepositoryLazy,
            hooksLazy = lazy { deps.hooks },
        )
    }

    val joinEventUseCaseLazy: Lazy<JoinEventUseCase> = lazy {
        JoinEventUseCase(
            transactionHelperLazy = transactionHelper,
            eventsLocalStoreLazy = eventsLocalStore,
            eventsRemoteStoreLazy = eventsRemoteStore,
            deleteEventUseCase = deleteEventUseCaseLazy,
            settingsRepositoryLazy = deps.settingsRepositoryLazy,
            currenciesLocalStoreLazy = currenciesLocalStore,
            currenciesPullTaskLazy = currenciesPullTask,
        )
    }

    val eventsInteractorLazy: Lazy<EventsInteractor> = lazy {
        EventsInteractor(
            eventsLocalStoreLazy = eventsLocalStore,
            currenciesLocalStoreLazy = currenciesLocalStore,
            settingsRepositoryLazy = deps.settingsRepositoryLazy,
        )
    }

}