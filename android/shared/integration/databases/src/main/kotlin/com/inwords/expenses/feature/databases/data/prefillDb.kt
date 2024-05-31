package com.inwords.expenses.feature.databases.data

import android.content.Context
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.databases.api.DatabasesComponent
import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// FIXME costyl
lateinit var appContext: Context

val dbComponent = DatabasesComponent(object : DatabasesComponent.Deps {
    override val context get() = appContext
})

fun prefillDb() {
    GlobalScope.launch(IO) {
        dbComponent.personsDao.insert(
            PersonEntity(
                personId = 1,
                name = "Василий",
            )
        )
        dbComponent.personsDao.insert(
            PersonEntity(
                personId = 2,
                name = "Максим",
            )
        )
        dbComponent.personsDao.insert(
            PersonEntity(
                personId = 3,
                name = "Анжела",
            )
        )
        dbComponent.personsDao.insert(
            PersonEntity(
                personId = 4,
                name = "Саша",
            )
        )

        dbComponent.currenciesDao.insert(
            CurrencyEntity(
                currencyId = 1,
                code = "USD",
                name = "US Dollar",
            )
        )
        dbComponent.currenciesDao.insert(
            CurrencyEntity(
                currencyId = 2,
                code = "EUR",
                name = "Euro",
            )
        )
        dbComponent.currenciesDao.insert(
            CurrencyEntity(
                currencyId = 3,
                code = "RUB",
                name = "Russian Ruble",
            )
        )

        dbComponent.eventsDao.insert(
            EventEntity(
                eventId = 1,
                name = "Fruska",
                primaryCurrencyId = 1,
                primaryPersonId = 1,
            )
        )
    }
}