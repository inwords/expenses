package com.inwords.expenses.feature.events.data.db.store

import com.inwords.expenses.feature.events.data.db.converter.toDomain
import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class CurrenciesLocalStoreImpl(
    currenciesDaoLazy: Lazy<CurrenciesDao>
) : CurrenciesLocalStore {

    private val currenciesDao by currenciesDaoLazy

    override fun getCurrencies(): Flow<List<Currency>> {
        return currenciesDao.queryAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }.distinctUntilChanged()
    }

    override suspend fun getCurrencyCodeById(currencyId: Long): String? {
        return currenciesDao.queryCodeById(currencyId)
    }

    override suspend fun insert(currencies: List<Currency>): List<Currency> {
        val currencyEntities = currencies.map { it.toEntity() }
        val ids = currenciesDao.insert(currencyEntities)

        return currencies.zip(ids) { currency, id ->
            id.takeIf { it != -1L }?.let { currency.copy(id = it) } ?: currency
        }
    }

}


