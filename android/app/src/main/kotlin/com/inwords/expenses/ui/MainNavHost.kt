package com.inwords.expenses.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.room.RoomDatabase
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.rememberNavigationController
import com.inwords.expenses.integration.databases.data.appContext
import com.inwords.expenses.integration.databases.data.dbComponent
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.events.data.db.dao.CurrenciesDao
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.ui.add_persons.addAddPersonsScreen
import com.inwords.expenses.feature.events.ui.create.addCreateEventScreen
import com.inwords.expenses.feature.events.ui.join.addJoinEventScreen
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.ui.add.addExpenseScreen
import com.inwords.expenses.feature.expenses.ui.debts_list.debtsListScreen
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenDestination
import com.inwords.expenses.feature.expenses.ui.list.expensesScreen
import com.inwords.expenses.feature.settings.api.SettingsComponentFactory
import com.inwords.expenses.feature.settings.api.SettingsRepository

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = ExpensesScreenDestination
) {
    val navigationController = rememberNavigationController(navController)

    val settingsComponent = remember { // TODO static DI
        SettingsComponentFactory(
            deps = object : SettingsComponentFactory.Deps {
                override val context: Context
                    get() = appContext
            }
        ).create()
    }

    val eventsComponent = remember {
        EventsComponent(
            deps = object : EventsComponent.Deps {
                override val eventsDao: EventsDao
                    get() = dbComponent.eventsDao
                override val personsDao: PersonsDao
                    get() = dbComponent.personsDao
                override val currenciesDao: CurrenciesDao
                    get() = dbComponent.currenciesDao

                override val db: RoomDatabase
                    get() = dbComponent.db

                override val settingsRepository: SettingsRepository
                    get() = settingsComponent.settingsRepository
            }
        )
    }

    val expensesComponent = remember {
        ExpensesComponent(
            deps = object : ExpensesComponent.Deps {
                override val expensesDao: ExpensesDao
                    get() = dbComponent.expensesDao
            }
        )
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        addJoinEventScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesScreenDestination = ExpensesScreenDestination,
        )
        addCreateEventScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesScreenDestination = ExpensesScreenDestination,
        )
        addAddPersonsScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesScreenDestination = ExpensesScreenDestination
        )

        addExpenseScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesInteractor = expensesComponent.expensesInteractor,
            settingsRepository = settingsComponent.settingsRepository,
        )
        expensesScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesInteractor = expensesComponent.expensesInteractor,
            settingsRepository = settingsComponent.settingsRepository,
        )
        debtsListScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesInteractor = expensesComponent.expensesInteractor,
        )
    }
}
