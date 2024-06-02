package com.inwords.expenses.feature.events.data

import com.inwords.expenses.feature.events.data.db.converter.toDomain
import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.domain.CurrenciesRepository
import com.inwords.expenses.feature.events.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CurrenciesRepositoryImpl(
    currenciesDaoLazy: Lazy<CurrenciesDao>
) : CurrenciesRepository {

    private val currenciesDao by currenciesDaoLazy

    override fun getCurrencies(): Flow<List<Currency>> {
        return currenciesDao.queryAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun insert(currencies: List<Currency>): List<Currency> {
        val currencyEntities = currencies.map { it.toEntity() }
        val ids = currenciesDao.insert(currencyEntities)

        return currencies.zip(ids) { currency, id ->
            id.takeIf { it != -1L }?.let { currency.copy(id = it) } ?: currency
        }
    }

}


