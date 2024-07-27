package com.inwords.expenses.core.network

import android.util.Log
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger

internal actual fun getLogger(): Logger = MessageLengthLimitingLogger(delegate = object : Logger {
    override fun log(message: String) {
        Log.v("Ktor", message)
    }
})
