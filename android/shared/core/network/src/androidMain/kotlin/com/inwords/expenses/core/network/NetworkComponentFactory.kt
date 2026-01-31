package com.inwords.expenses.core.network

import android.content.Context

actual class NetworkComponentFactory(
    private val context: Context,
    private val production: Boolean
) {

    actual fun create(): NetworkComponent {
        return NetworkComponent(HttpClientFactory(context, enableLogging = !production))
    }
}
