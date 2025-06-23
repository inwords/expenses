package com.inwords.expenses

import android.app.Application
import androidx.work.Configuration
import com.inwords.expenses.integration.base.enableSync
import com.inwords.expenses.integration.base.initializeSentry
import com.inwords.expenses.integration.base.registerComponents
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.asExecutor

class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        initializeSentry(production = !BuildConfig.DEBUG)

        registerComponents(this)

        enableSync()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerCoroutineContext(IO)
            .setTaskExecutor(IO.limitedParallelism(4).asExecutor())
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) {
                    android.util.Log.INFO
                } else {
                    android.util.Log.ASSERT
                }
            )
            .build()
}
