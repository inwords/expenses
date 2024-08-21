package com.inwords.expenses

import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.api.EventsComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@OptIn(DelicateCoroutinesApi::class)
internal fun enableSync() {
    val scope = GlobalScope + IO

    scope.launch {
        ComponentsMap.getComponent<EventsComponent>()
            .eventsSyncObserver
            .observeNewEventsIn(scope)
    }
}