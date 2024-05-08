package com.inwords.expenses.feature.databases.api

import android.content.Context
import com.inwords.expenses.feature.databases.data.createAppDatabase
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao

class DatabasesComponent(private val deps: Deps) {

    interface Deps {
        val context: Context
    }

    private val db by lazy {
        createAppDatabase(deps.context)
    }

    // Room DAOs are lazy initialized by Room itself
    val eventsDao: EventsDao get() = db.eventsDao()
    val expensesDao: ExpensesDao get() = db.expensesDao()
    val currenciesDao: CurrenciesDao get() = db.currenciesDao()
    val personsDao: PersonsDao get() = db.personsDao()
}