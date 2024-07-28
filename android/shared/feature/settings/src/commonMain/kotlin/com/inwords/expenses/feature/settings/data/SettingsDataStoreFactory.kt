package com.inwords.expenses.feature.settings.data

import androidx.datastore.core.DataStore

internal expect class SettingsDataStoreFactory {

    fun createSettingsDataStore(): DataStore<Settings>
}