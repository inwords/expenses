package com.inwords.expenses.core.network

import io.ktor.client.plugins.logging.Logger

internal actual fun getLogger(): Logger = object : Logger {

    override fun log(message: String) {
        println("Ktor Logger: $message")
    }
}