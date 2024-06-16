package com.inwords.expenses.feature.expenses.ui.list

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.DefaultNavigationController
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.Serializable

@Serializable
object ExpensesScreenDestination : Destination

fun NavGraphBuilder.expensesScreen(
    navigationController: DefaultNavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) {
    composable<ExpensesScreenDestination> {
        val viewModel = viewModel<ExpensesViewModel>(it, factory = viewModelFactory {
            initializer {
                ExpensesViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesInteractor = expensesInteractor,
                    settingsRepository = settingsRepository,
                )
            }
        })
        ExpensesScreen(
            onAddExpenseClick = viewModel::onAddExpenseClick,
            state = viewModel.state.collectAsStateWithLifecycle().value
        )
    }
}