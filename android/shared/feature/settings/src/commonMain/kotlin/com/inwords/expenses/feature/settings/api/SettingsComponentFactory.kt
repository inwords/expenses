package com.inwords.expenses.feature.settings.api


expect class SettingsComponentFactory {

    interface Deps

    fun create(): SettingsComponent
}
