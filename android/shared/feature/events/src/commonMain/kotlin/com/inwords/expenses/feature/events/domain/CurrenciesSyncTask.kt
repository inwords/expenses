package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CurrenciesSyncTask internal constructor(
    private val currenciesLocalStore: CurrenciesLocalStore,
    private val currenciesRemoteStore: CurrenciesRemoteStore,
) {

    suspend fun syncCurrencies(): Boolean = withContext(IO) {
        val networkDeferred = async { currenciesRemoteStore.getCurrencies() }

        val localCurrencies = currenciesLocalStore.getCurrencies().first().associateBy { it.code }

        val networkCurrencies = when (val networkResult = networkDeferred.await()) {
            is Result.Success -> networkResult.data
            is Result.Error -> return@withContext false
        }

        var hasUpdates = false
        val updatedCurrencies = networkCurrencies.map { networkCurrency ->
            val localCurrency = localCurrencies[networkCurrency.code]
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
        }

        true
    }
}