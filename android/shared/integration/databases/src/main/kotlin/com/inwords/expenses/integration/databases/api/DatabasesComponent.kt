package com.inwords.expenses.integration.databases.api

import android.content.Context
import androidx.room.RoomDatabase
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.integration.databases.data.createAppDatabase

class DatabasesComponent(private val deps: Deps) {

    interface Deps {
        val context: Context
    }

    private val roomDb by lazy {
        createAppDatabase(deps.context)
    }

    val db: RoomDatabase get() = roomDb

    // Room DAOs are lazy initialized by Room itself
    val eventsDao: EventsDao get() = roomDb.eventsDao()
    val expensesDao: ExpensesDao get() = roomDb.expensesDao()
    val currenciesDao: CurrenciesDao get() = roomDb.currenciesDao()
    val personsDao: PersonsDao get() = roomDb.personsDao()
}