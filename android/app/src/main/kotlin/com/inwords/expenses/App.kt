package com.inwords.expenses

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerComponents(this)

        enableSync()
    }

}
