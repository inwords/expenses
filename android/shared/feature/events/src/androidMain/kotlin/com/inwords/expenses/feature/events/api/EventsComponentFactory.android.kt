package com.inwords.expenses.feature.events.api

import android.content.Context
import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.data.EventsSyncManagerFactory
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.settings.api.SettingsRepository
import io.ktor.client.HttpClient

actual class EventsComponentFactory(private val deps: Deps) {

    actual interface Deps {
        val context: Context

        actual val eventsDao: EventsDao
        actual val personsDao: PersonsDao
        actual val currenciesDao: CurrenciesDao

        actual val transactionHelper: TransactionHelper

        actual val client: SuspendLazy<HttpClient>
        actual val hostConfig: HostConfig

        actual val settingsRepository: SettingsRepository
    }

    actual fun create(): EventsComponent {
        val syncManagerFactory = EventsSyncManagerFactory(deps.context)
        return EventsComponent(eventsSyncManagerFactory = syncManagerFactory, deps = deps)
    }

}