package com.inwords.expenses.feature.databases.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.EventCurrencyCrossRef
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.EventPersonCrossRef
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseEntity
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseSubjectPersonCrossRef
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Database(
    entities = [
        EventEntity::class,
        ExpenseEntity::class,
        CurrencyEntity::class,
        PersonEntity::class,
        EventCurrencyCrossRef::class,
        EventPersonCrossRef::class,
        ExpenseSubjectPersonCrossRef::class,
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun eventsDao(): EventsDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun currenciesDao(): CurrenciesDao
    abstract fun personsDao(): PersonsDao
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun createAppDatabase(context: Context): AppDatabase {
    val queryCoroutineContext = IO.limitedParallelism(4)

    return Room.databaseBuilder(context, "app_db") { AppDatabase::class.instantiateImpl() }
        .setQueryCoroutineContext(queryCoroutineContext)
        .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
        .fallbackToDestructiveMigration(dropAllTables = true) // FIXME remove before prod
        .build()
}