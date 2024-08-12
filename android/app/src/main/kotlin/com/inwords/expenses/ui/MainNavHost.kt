package com.inwords.expenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.rememberNavigationController
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.events.ui.add_persons.addAddPersonsScreen
import com.inwords.expenses.feature.events.ui.create.addCreateEventScreen
import com.inwords.expenses.feature.events.ui.join.addJoinEventScreen
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.ui.add.addExpenseScreen
import com.inwords.expenses.feature.expenses.ui.debts_list.debtsListScreen
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenDestination
import com.inwords.expenses.feature.expenses.ui.list.expensesScreen
import com.inwords.expenses.feature.settings.api.SettingsComponent

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = ExpensesScreenDestination
) {
    val navigationController = rememberNavigationController(navController)

    val settingsComponent = remember { ComponentsMap.getComponent<SettingsComponent>() }
    val eventsComponent = remember { ComponentsMap.getComponent<EventsComponent>() }
    val expensesComponent = remember { ComponentsMap.getComponent<ExpensesComponent>() }

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
