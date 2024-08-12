package com.inwords.expenses

import android.app.Application
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.feature.events.api.EventsComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerComponents(this)

        ComponentsMap.getComponent<EventsComponent>().eventsSyncInteractor.syncEvents() // TODO remove
    }

}
