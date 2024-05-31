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

    override suspend fun insert(currency: Currency): Currency {
        val id = currenciesDao.insert(currency.toEntity()).takeIf { it != -1L }
        return if (id == null) {
            currency
        } else {
            currency.copy(id = id)
        }
    }

}


