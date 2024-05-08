package com.inwords.expenses.core.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NetworkFactory {

    fun createOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun createKtor(okHttp: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                this.preconfigured = okHttp
                this.pipelining = true

                config {
                    dispatcher(okHttp.dispatcher) // overridden by ktor for some reason
                }
            }

            install(ContentNegotiation) {
                json()
            }

            install(Logging) {
                logger = MessageLengthLimitingLogger(delegate = object : Logger {
                    override fun log(message: String) {
                        Log.v("Ktor", message)
                    }
                })
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }

}