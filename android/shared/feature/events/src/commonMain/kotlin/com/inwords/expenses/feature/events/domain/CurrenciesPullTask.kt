package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CurrenciesPullTask internal constructor(
    private val currenciesLocalStore: CurrenciesLocalStore,
    private val currenciesRemoteStore: CurrenciesRemoteStore,
) {

    suspend fun pullCurrencies(): Boolean = withContext(IO) {
        val networkCurrencies = when (val networkResult = currenciesRemoteStore.getCurrencies()) {
            is Result.Success -> networkResult.data
            is Result.Error -> return@withContext false
        }

        updateLocalCurrencies(networkCurrencies)

        true
    }

    internal suspend fun updateLocalCurrencies(networkCurrencies: List<Currency>): List<Currency> = withContext(IO) {
        val localCurrencies = currenciesLocalStore.getCurrencies().first()
        val localCurrenciesMap = localCurrencies.associateBy { it.code }

        var hasUpdates = false
        val updatedCurrencies = networkCurrencies.map { networkCurrency ->
            val localCurrency = localCurrenciesMap[networkCurrency.code]
            if (localCurrency != null) {
                if (localCurrency.serverId == 0L) {
                    hasUpdates = true
                    localCurrency.copy(serverId = networkCurrency.serverId)
                } else {
                    localCurrency
                }
            } else {
                hasUpdates = true
                networkCurrency
            }
        }

        if (hasUpdates) {
            currenciesLocalStore.insert(updatedCurrencies)
        } else {
            localCurrencies
        }
    }

}