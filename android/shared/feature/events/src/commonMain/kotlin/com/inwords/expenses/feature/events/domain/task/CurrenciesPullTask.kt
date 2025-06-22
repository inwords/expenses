package com.inwords.expenses.feature.events.domain.task

import com.inwords.expenses.core.storage.utils.TransactionHelper
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CurrenciesPullTask internal constructor(
    transactionHelperLazy: Lazy<TransactionHelper>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
    currenciesRemoteStoreLazy: Lazy<CurrenciesRemoteStore>,
) {

    private val transactionHelper by transactionHelperLazy
    private val currenciesLocalStore by currenciesLocalStoreLazy
    private val currenciesRemoteStore by currenciesRemoteStoreLazy

    suspend fun pullCurrencies(): IoResult<List<Currency>> = withContext(IO) {
        val networkCurrencies = when (val networkResult = currenciesRemoteStore.getCurrencies()) {
            is IoResult.Success -> networkResult.data
            is IoResult.Error -> return@withContext networkResult
        }

        val currencies = transactionHelper.immediateWriteTransaction {
            updateLocalCurrencies(networkCurrencies)
        }

        IoResult.Success(currencies)
    }

    private suspend fun updateLocalCurrencies(
        networkCurrencies: List<Currency>
    ): List<Currency> {
        val localCurrencies = currenciesLocalStore.getCurrencies().first()
        val localCurrenciesMap = localCurrencies.associateBy { it.code }

        var hasUpdates = false
        val updatedCurrencies = networkCurrencies.map { networkCurrency ->
            val localCurrency = localCurrenciesMap[networkCurrency.code]
            if (localCurrency != null) {
                if (localCurrency.serverId == null) {
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

        return if (hasUpdates) {
            currenciesLocalStore.insert(updatedCurrencies)
        } else {
            localCurrencies
        }
    }

}