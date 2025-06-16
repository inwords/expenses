package com.inwords.expenses.feature.events.data.network.store

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.requestWithExceptionHandling
import com.inwords.expenses.core.network.toIoResult
import com.inwords.expenses.core.network.url
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.data.network.dto.CurrencyDto
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class CurrenciesRemoteStoreImpl(
    private val client: SuspendLazy<HttpClient>,
    private val hostConfig: HostConfig,
) : CurrenciesRemoteStore {

    override suspend fun getCurrencies(): IoResult<List<Currency>> {
        return client.requestWithExceptionHandling {
            get {
                url(hostConfig) {
                    pathSegments = listOf("api", "user", "currencies", "all")
                }
            }.body<List<CurrencyDto>>().map { it.toCurrency() }
        }.toIoResult()
    }

    private fun CurrencyDto.toCurrency(): Currency {
        return Currency(
            id = 0L,
            serverId = id,
            code = code,
            name = code, // FIXME: get name from dictionary
        )
    }

}