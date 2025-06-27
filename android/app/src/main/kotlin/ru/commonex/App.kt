package ru.commonex

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.integration.base.enableSync
import com.inwords.expenses.integration.base.initializeSentry
import com.inwords.expenses.integration.base.registerComponents
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
                    Log.INFO
                } else {
                    Log.ASSERT
                }
            )
            .build()
}