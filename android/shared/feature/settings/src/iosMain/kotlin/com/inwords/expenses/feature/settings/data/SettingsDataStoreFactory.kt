package com.inwords.expenses.feature.settings.data

import androidx.datastore.core.DataStore
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

internal actual class SettingsDataStoreFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createSettingsDataStore(): DataStore<Settings> {
        return settingsDataStore.getOrCreate {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/$settingsDsFileName").toPath()
        }
    }

}