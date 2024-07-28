package com.inwords.expenses.feature.settings.api

import com.inwords.expenses.feature.settings.data.SettingsDataStoreFactory

actual class SettingsComponentFactory {

    actual interface Deps

    actual fun create(): SettingsComponent {
        return SettingsComponent(SettingsDataStoreFactory())
    }
}
