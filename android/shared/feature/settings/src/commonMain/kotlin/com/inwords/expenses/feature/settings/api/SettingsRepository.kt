package com.inwords.expenses.feature.settings.api

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setCurrentEventId(eventId: Long)
    fun getCurrentEventId(): Flow<Long?>

    suspend fun setCurrentPersonId(userId: Long)
    fun getCurrentPersonId(): Flow<Long?>

    suspend fun clearCurrentEventAndPerson()
}
