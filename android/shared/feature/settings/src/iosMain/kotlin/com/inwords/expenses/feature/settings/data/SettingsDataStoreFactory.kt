package com.inwords.expenses.feature.settings.data

import androidx.datastore.core.DataStore

internal actual class SettingsDataStoreFactory {

    actual fun createSettingsDataStore(): DataStore<Settings> {
        return settingsDataStore.getOrCreate {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory, // TODO run on mac and fix
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/$settingsDsFileName").toPath()
        }
    }

}