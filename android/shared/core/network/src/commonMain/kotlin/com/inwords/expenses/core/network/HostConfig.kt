package com.inwords.expenses.core.network

import io.ktor.http.URLProtocol

data class HostConfig(
    val protocol: URLProtocol,
    val host: String,
)