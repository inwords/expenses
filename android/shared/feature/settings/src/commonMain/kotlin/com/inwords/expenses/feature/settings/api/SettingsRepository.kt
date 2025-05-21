package com.inwords.expenses.feature.settings.api

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setCurrentEventId(eventId: Long)
    suspend fun clearCurrentEventId()
    fun getCurrentEventId(): Flow<Long?>

    suspend fun setCurrentPersonId(userId: Long)
    suspend fun clearCurrentPersonId()
    fun getCurrentPersonId(): Flow<Long?>
}
