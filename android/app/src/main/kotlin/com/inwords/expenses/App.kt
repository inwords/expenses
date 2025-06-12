package com.inwords.expenses

import android.app.Application
import com.inwords.expenses.integration.base.enableSync
import com.inwords.expenses.integration.base.registerComponents

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerComponents(this)

        enableSync()
    }

}
