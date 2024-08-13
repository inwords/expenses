package com.inwords.expenses

import android.app.Application
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.feature.events.api.EventsComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerComponents(this)

        // FIXME not here
        GlobalScope.launch {
            ComponentsMap.getComponent<EventsComponent>().currenciesSyncTask.syncCurrencies()
        }
    }

}
