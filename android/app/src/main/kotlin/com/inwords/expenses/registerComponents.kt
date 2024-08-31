package com.inwords.expenses

import android.content.Context
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.registerComponent
import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.NetworkComponentFactory
import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.api.EventsComponentFactory
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.settings.api.SettingsComponentFactory
import com.inwords.expenses.feature.settings.api.SettingsRepository
import com.inwords.expenses.feature.sync.api.SyncComponentFactory
import com.inwords.expenses.integration.databases.api.DatabasesComponentFactory
import io.ktor.client.HttpClient

internal fun registerComponents(appContext: Context) {
    val settingsComponent = lazy {
        SettingsComponentFactory(
            deps = object : SettingsComponentFactory.Deps {
                override val context: Context
                    get() = appContext
            }
        ).create()
    }

    val dbComponent = lazy {
        DatabasesComponentFactory(object : DatabasesComponentFactory.Deps {
            override val context get() = appContext
        }).create()
    }

    val networkComponent = lazy {
        NetworkComponentFactory(appContext).create()
    }

    val eventsComponent = lazy {
        EventsComponentFactory(
            deps = object : EventsComponentFactory.Deps {
                override val eventsDao: EventsDao
                    get() = dbComponent.value.eventsDao
                override val personsDao: PersonsDao
                    get() = dbComponent.value.personsDao
                override val currenciesDao: CurrenciesDao
                    get() = dbComponent.value.currenciesDao

                override val transactionHelper: TransactionHelper
                    get() = dbComponent.value.transactionHelper

                override val client: SuspendLazy<HttpClient>
                    get() = SuspendLazy { networkComponent.value.getHttpClient() }
                override val hostConfig: HostConfig
                    get() = networkComponent.value.hostConfig

                override val settingsRepository: SettingsRepository
                    get() = settingsComponent.value.settingsRepository
            }
        ).create()
    }

    val expensesComponent = lazy {
        ExpensesComponent(
            deps = object : ExpensesComponent.Deps {
                override val expensesDao: ExpensesDao
                    get() = dbComponent.value.expensesDao

                override val client: SuspendLazy<HttpClient>
                    get() = SuspendLazy { networkComponent.value.getHttpClient() }
                override val hostConfig: HostConfig
                    get() = networkComponent.value.hostConfig

                override val eventsLocalStore: EventsLocalStore
                    get() = eventsComponent.value.eventsLocalStore.value
            }
        )
    }

    val syncComponent = lazy {
        SyncComponentFactory(
            deps = object : SyncComponentFactory.Deps {
                override val context: Context get() = appContext

                override val eventsInteractor: EventsInteractor
                    get() = eventsComponent.value.eventsInteractor
                override val expensesInteractor: ExpensesInteractor
                    get() = expensesComponent.value.expensesInteractor

            }
        ).create()
    }

    ComponentsMap.registerComponent(settingsComponent)
    ComponentsMap.registerComponent(dbComponent)
    ComponentsMap.registerComponent(networkComponent)
    ComponentsMap.registerComponent(eventsComponent)
    ComponentsMap.registerComponent(expensesComponent)
    ComponentsMap.registerComponent(syncComponent)
}