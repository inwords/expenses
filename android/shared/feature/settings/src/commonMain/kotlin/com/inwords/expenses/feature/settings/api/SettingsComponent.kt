package com.inwords.expenses.feature.settings.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.settings.data.SettingsDataStoreFactory
import com.inwords.expenses.feature.settings.data.SettingsLocalDataSource
import com.inwords.expenses.feature.settings.data.SettingsRepositoryImpl

class SettingsComponent internal constructor(
    private val settingsDataStoreFactory: SettingsDataStoreFactory
) : Component {

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(
            settingsLocalDataSource = SettingsLocalDataSource(
                lazy { settingsDataStoreFactory.createSettingsDataStore() }
            )
        )
    }
}