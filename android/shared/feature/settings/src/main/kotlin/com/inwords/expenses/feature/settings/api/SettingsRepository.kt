package com.inwords.expenses.feature.settings.api

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setCurrentEventId(eventId: Long)

    fun getCurrentEventId(): Flow<Long?>
}
