package com.inwords.expenses.feature.expenses.api

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.expenses.data.db.ExpensesLocalStoreImpl
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.data.network.ExpensesRemoteStoreImpl
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import com.inwords.expenses.feature.expenses.domain.store.ExpensesRemoteStore
import com.inwords.expenses.feature.expenses.domain.tasks.EventExpensesPullTask
import com.inwords.expenses.feature.expenses.domain.tasks.EventExpensesPushTask
import io.ktor.client.HttpClient

class ExpensesComponent(private val deps: Deps) : Component {

    interface Deps {

        val expensesDao: ExpensesDao

        val client: SuspendLazy<HttpClient>
        val hostConfig: HostConfig

        val eventsLocalStore: EventsLocalStore
    }

    private val expensesLocalStore: Lazy<ExpensesLocalStore> = lazy {
        ExpensesLocalStoreImpl(lazy { deps.expensesDao })
    }

    private val expensesRemoteStore: Lazy<ExpensesRemoteStore> = lazy {
        ExpensesRemoteStoreImpl(
            client = deps.client,
            hostConfig = deps.hostConfig,
        )
    }

    val eventExpensesPushTask: Lazy<EventExpensesPushTask> = lazy {
        EventExpensesPushTask(
            eventsLocalStoreLazy = lazy { deps.eventsLocalStore },
            expensesLocalStoreLazy = expensesLocalStore,
            expensesRemoteStoreLazy = expensesRemoteStore
        )
    }

    val eventExpensesPullTask: Lazy<EventExpensesPullTask> = lazy {
        EventExpensesPullTask(
            eventsLocalStoreLazy = lazy { deps.eventsLocalStore },
            expensesLocalStoreLazy = expensesLocalStore,
            expensesRemoteStoreLazy = expensesRemoteStore
        )
    }

    val expensesInteractor: ExpensesInteractor by lazy {
        ExpensesInteractor(expensesLocalStore)
    }
}