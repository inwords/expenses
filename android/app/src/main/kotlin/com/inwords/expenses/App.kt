package com.inwords.expenses

import android.app.Application
import android.content.Context
import com.inwords.expenses.integration.databases.api.DatabasesComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this

        prefillDb() // TODO
    }

}

// FIXME costyl
lateinit var appContext: Context

val dbComponent = DatabasesComponent(object : DatabasesComponent.Deps {
    override val context get() = appContext
})