package com.inwords.expenses

import android.app.Application
import android.content.Context
import com.inwords.expenses.integration.databases.api.DatabasesComponentFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this

        prefillDb() // TODO
    }

}

// FIXME costyl
lateinit var appContext: Context

val dbComponent by lazy {
    DatabasesComponentFactory(object : DatabasesComponentFactory.Deps {
        override val context get() = appContext
    }).create()
}