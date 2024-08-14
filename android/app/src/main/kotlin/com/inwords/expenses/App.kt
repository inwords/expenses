package com.inwords.expenses

import android.app.Application
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.api.EventsComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerComponents(this)

        // TODO move from here
        GlobalScope.launch(IO) {
            ComponentsMap.getComponent<EventsComponent>().eventsSyncObserver.observeNewEventsIn(this)
        }
    }

}
