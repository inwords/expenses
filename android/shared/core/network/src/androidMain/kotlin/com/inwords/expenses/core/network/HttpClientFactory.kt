package com.inwords.expenses.core.network

import android.content.Context
import com.inwords.expenses.core.ktor_client_cronet.Cronet
import com.inwords.expenses.core.utils.IO
import io.ktor.client.HttpClient
import kotlinx.coroutines.withContext
import org.chromium.net.ConnectionMigrationOptions
import org.chromium.net.CronetEngine
import org.chromium.net.DnsOptions
import java.io.File

internal actual class HttpClientFactory(private val context: Context) {

    actual suspend fun createHttpClient(): HttpClient {
        return withContext(IO) {
            val cronet = createCronet(context)
            createKtor(cronet)
        }
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
            }
        }
    }

}