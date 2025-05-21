package com.inwords.expenses.feature.settings.data

import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.flow.Flow

internal class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override suspend fun setCurrentEventId(eventId: Long) {
        settingsLocalDataSource.setCurrentEventId(eventId)
    }

    override suspend fun clearCurrentEventId() {
        settingsLocalDataSource.setCurrentEventId(-1)
    }

    override fun getCurrentEventId(): Flow<Long?> {
        return settingsLocalDataSource.getCurrentEventId()
    }

    override suspend fun setCurrentPersonId(userId: Long) {
        settingsLocalDataSource.setCurrentPersonId(userId)
    }

    override suspend fun clearCurrentPersonId() {
        settingsLocalDataSource.setCurrentPersonId(-1)
    }

    override fun getCurrentPersonId(): Flow<Long?> {
        return settingsLocalDataSource.getCurrentPersonId()
    }
}