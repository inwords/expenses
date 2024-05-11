package com.inwords.expenses.core.network

import android.content.Context
import android.util.Log
import com.inwords.expenses.core.ktor_client_cronet.Cronet
import com.inwords.expenses.core.utils.IO
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withContext
import org.chromium.net.ConnectionMigrationOptions
import org.chromium.net.CronetEngine
import org.chromium.net.DnsOptions
import java.io.File

class NetworkFactory {

    suspend fun createHttpClient(context: Context) = withContext(IO) {
        val cronet = createCronet(context)
        createKtor(cronet)
    }

    private fun createCronet(context: Context): CronetEngine {
        val storagePath = context.cacheDir.absolutePath + "/cronet"
        val storageFile = File(storagePath)
        if (!storageFile.exists()) {
            storageFile.mkdirs()
        }

        return CronetEngine.Builder(context)
            .enableHttp2(true)
            .enableQuic(true)
            .enableBrotli(true)
            .setConnectionMigrationOptions(
                ConnectionMigrationOptions.builder()
                    .enableDefaultNetworkMigration(true)
                    .build()
            )
            .setDnsOptions(
                DnsOptions.builder()
                    .persistHostCache(true)
                    .build()
            )
            .setStoragePath(storagePath)
            .setUserAgent("Android/Expenses/1.0") // TODO choose good user agent
            .build()
    }

    private fun createKtor(cronetEngine: CronetEngine): HttpClient {
        return createKtor(Cronet(cronetEngine)) {
            engine {
                this.pipelining = true
                this.followRedirects = false
                this.threadsCount = 24
            }
        }
    }

    private fun <T : HttpClientEngineConfig> createKtor(
        httpClientEngine: HttpClientEngineFactory<T>,
        block: HttpClientConfig<T>.() -> Unit
    ): HttpClient {
        return HttpClient(httpClientEngine) {
            block.invoke(this)

            followRedirects = false

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
        }
    }

}