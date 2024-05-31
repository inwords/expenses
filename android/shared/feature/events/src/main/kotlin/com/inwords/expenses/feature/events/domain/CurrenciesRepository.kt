package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency
import kotlinx.coroutines.flow.Flow

internal interface CurrenciesRepository {

    fun getCurrencies(): Flow<List<Currency>>

    suspend fun insert(currency: Currency): Currency
}