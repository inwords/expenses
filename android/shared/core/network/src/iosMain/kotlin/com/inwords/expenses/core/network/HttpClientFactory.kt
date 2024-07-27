package com.inwords.expenses.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual class HttpClientFactory {

    actual suspend fun createHttpClient(): HttpClient {
        return createKtor(Darwin) {
            engine {
                this.pipelining = true
                this.threadsCount = 24
            }
        }
    }

}