package com.inwords.expenses

import android.app.Application
import com.inwords.expenses.integration.databases.data.appContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this

        prefillDb() // TODO
    }

}