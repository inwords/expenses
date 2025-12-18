package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.settings.api.SettingsRepository

class LeaveEventUseCase internal constructor(
    settingsRepositoryLazy: Lazy<SettingsRepository>,
) {
    private val settingsRepository by settingsRepositoryLazy

    suspend fun leaveEvent() {
        settingsRepository.clearCurrentEventAndPerson()
    }
}

