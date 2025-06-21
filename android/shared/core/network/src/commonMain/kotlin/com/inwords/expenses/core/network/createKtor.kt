package com.inwords.expenses.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun <T : HttpClientEngineConfig> createKtor(
    httpClientEngine: HttpClientEngineFactory<T>,
    block: HttpClientConfig<T>.() -> Unit
): HttpClient {
    return HttpClient(httpClientEngine) {
        block.invoke(this)

        expectSuccess = true

        followRedirects = false

        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }

        install(Logging) {
            logger = getLogger()
            level = LogLevel.NONE
        }
    }
}