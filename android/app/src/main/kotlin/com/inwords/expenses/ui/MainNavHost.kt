package com.inwords.expenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.inwords.expenses.core.navigation.DefaultNavigationController
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.feature.databases.data.dbComponent
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.data.db.dao.ExpensesDao
import com.inwords.expenses.feature.expenses.ui.add.addExpenseScreen
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenDestination
import com.inwords.expenses.feature.expenses.ui.list.expensesScreen
import com.inwords.expenses.ui.home.HomeScreenDestination
import com.inwords.expenses.ui.home.homeScreen


@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = HomeScreenDestination
) {
    val navigationController = remember { DefaultNavigationController(navController) }

    val eventsComponent = remember { EventsComponent(deps = object : EventsComponent.Deps {}) }

    val expensesComponent = remember {
        ExpensesComponent(
            deps = object : ExpensesComponent.Deps {
                override val eventsInteractor: EventsInteractor
                    get() = eventsComponent.eventsInteractor
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
        homeScreen( // TODO tmp screen
            onNavigateToExpenses = { navController.navigate(ExpensesScreenDestination) }
        )
        addExpenseScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesInteractor = expensesComponent.expensesInteractor,
            homeScreenDestination = HomeScreenDestination,
        )
        expensesScreen(
            navigationController = navigationController,
            eventsInteractor = eventsComponent.eventsInteractor,
            expensesInteractor = expensesComponent.expensesInteractor,
            homeScreenDestination = HomeScreenDestination,
        )
    }
}
