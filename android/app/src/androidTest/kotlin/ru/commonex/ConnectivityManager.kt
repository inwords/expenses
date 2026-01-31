package ru.commonex

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.test.platform.app.InstrumentationRegistry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal object ConnectivityManager {

    fun turnOffDataAndWifi() {
        sh("svc wifi disable")
        sh("svc data disable")
        awaitNoValidatedNetwork()
    }

    fun turnOnData() {
        sh("svc data enable")
        sh("svc wifi enable")
    }

    private fun sh(cmd: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.uiAutomation.executeShellCommand(cmd).use { /* close fd */ }
    }

    private fun awaitNoValidatedNetwork() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val cm = appContext.getSystemService(ConnectivityManager::class.java)

        cm.activeNetwork ?: return

        val latch = CountDownLatch(1)
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                latch.countDown()
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                    latch.countDown()
                }
            }
        }

        cm.registerDefaultNetworkCallback(callback)
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                error("Timed out waiting for device to go offline")
            }
        } finally {
            cm.unregisterNetworkCallback(callback)
        }
    }

}
