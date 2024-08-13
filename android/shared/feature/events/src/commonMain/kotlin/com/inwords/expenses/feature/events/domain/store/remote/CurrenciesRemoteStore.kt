package com.inwords.expenses.feature.events.domain.store.remote

import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.model.Currency

internal interface CurrenciesRemoteStore {

    suspend fun getCurrencies(): Result<List<Currency>>

}