package com.inwords.expenses.core.network

actual class NetworkComponentFactory {

    actual fun create(): NetworkComponent {
        return NetworkComponent(HttpClientFactory())
    }
}
