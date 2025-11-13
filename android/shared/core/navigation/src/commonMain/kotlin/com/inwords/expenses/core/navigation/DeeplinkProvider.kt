package com.inwords.expenses.core.navigation

import kotlinx.coroutines.flow.Flow

expect class DeeplinkProvider {

    fun latestDeeplink(): Flow<String>
}
