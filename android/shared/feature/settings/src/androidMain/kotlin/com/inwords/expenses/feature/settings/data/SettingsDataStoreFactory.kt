package com.inwords.expenses.feature.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import okio.Path.Companion.toPath


internal actual class SettingsDataStoreFactory(
    private val context: Context
) {

    actual fun createSettingsDataStore(): DataStore<Settings> {
        return settingsDataStore.getOrCreate { context.dataStoreFile(settingsDsFileName).absolutePath.toPath() }
    }
}
