package com.inwords.expenses.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual class HttpClientFactory {

    actual suspend fun createHttpClient(): HttpClient {
        // FIXME: check if content-encoding is automatically handled by Darwin engine
        return createKtor(Darwin) {
            engine {
                this.pipelining = true
            }
        }
    }

}