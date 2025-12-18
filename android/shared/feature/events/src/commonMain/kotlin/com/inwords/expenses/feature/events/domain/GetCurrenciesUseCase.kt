package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import kotlinx.coroutines.flow.Flow

class GetCurrenciesUseCase internal constructor(
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
) {
    private val currenciesLocalStore by currenciesLocalStoreLazy

    fun getCurrencies(): Flow<List<Currency>> {
        return currenciesLocalStore.getCurrencies()
    }
}
