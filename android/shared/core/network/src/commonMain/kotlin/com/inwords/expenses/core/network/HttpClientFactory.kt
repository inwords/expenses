package com.inwords.expenses.core.network

import io.ktor.client.HttpClient

internal expect class HttpClientFactory {

    suspend fun createHttpClient(): HttpClient
}