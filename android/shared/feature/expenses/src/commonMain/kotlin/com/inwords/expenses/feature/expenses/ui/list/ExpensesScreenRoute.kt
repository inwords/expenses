package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.Serializable

@Serializable
object ExpensesScreenDestination : Destination

fun NavGraphBuilder.addExpensesScreen(
    navigationController: NavigationController,
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
            state = viewModel.state.collectAsState().value, // FIXME: collectAsStateWithLifecycle has issues with Compose Navigation
            onMenuClick = viewModel::onMenuClick,
            onAddExpenseClick = viewModel::onAddExpenseClick,
            onRevertExpenseClick = viewModel::onRevertExpenseClick,
            onDebtsDetailsClick = viewModel::onDebtsDetailsClick,
            onReplenishmentClick = viewModel::onReplenishmentClick,
            onCreateEventClick = viewModel::onCreateEventClick,
            onJoinEventClick = viewModel::onJoinEventClick,
            onRefresh = viewModel::onRefresh,
        )
    }
}