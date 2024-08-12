package com.inwords.expenses

import android.app.Application
import android.content.Context
import com.inwords.expenses.integration.databases.api.DatabasesComponentFactory
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this
        NetworkTest.sendTest(this)
        registerComponents(this)

    }

}
