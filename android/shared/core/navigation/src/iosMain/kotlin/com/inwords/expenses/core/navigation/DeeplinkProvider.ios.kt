package com.inwords.expenses.core.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class DeeplinkProvider() {

    actual fun latestDeeplink(): Flow<String> {
        return emptyFlow()
    }
}
