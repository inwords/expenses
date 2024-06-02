package com.inwords.expenses.feature.settings.api

import android.content.Context
import com.inwords.expenses.feature.settings.data.SettingsLocalDataSource
import com.inwords.expenses.feature.settings.data.SettingsRepositoryImpl

class SettingsComponent(private val deps: Deps) {

    interface Deps {

        val context: Context
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(
            settingsLocalDataSource = SettingsLocalDataSource(deps.context)
        )
    }
}