package com.inwords.expenses

import android.content.Context
import androidx.room.RoomDatabase
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.registerComponent
import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.NetworkComponentFactory
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.api.EventsComponentFactory
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.settings.api.SettingsComponentFactory
import com.inwords.expenses.feature.settings.api.SettingsRepository
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
                override val context: Context
                    get() = appContext

                override val eventsDao: EventsDao
                    get() = dbComponent.value.eventsDao
                override val personsDao: PersonsDao
                    get() = dbComponent.value.personsDao
                override val currenciesDao: CurrenciesDao
                    get() = dbComponent.value.currenciesDao

                override val db: RoomDatabase
                    get() = dbComponent.value.db

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
            }
        )
    }

    ComponentsMap.registerComponent(settingsComponent)
    ComponentsMap.registerComponent(dbComponent)
    ComponentsMap.registerComponent(networkComponent)
    ComponentsMap.registerComponent(eventsComponent)
    ComponentsMap.registerComponent(expensesComponent)
}