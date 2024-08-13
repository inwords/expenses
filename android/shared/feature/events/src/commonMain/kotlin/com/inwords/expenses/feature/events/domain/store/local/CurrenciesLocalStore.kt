package com.inwords.expenses.feature.events.domain.store.local

import com.inwords.expenses.feature.events.domain.model.Currency
import kotlinx.coroutines.flow.Flow

internal interface CurrenciesLocalStore {

    fun getCurrencies(): Flow<List<Currency>>

    suspend fun insert(currencies: List<Currency>): List<Currency>
}