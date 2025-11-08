package com.inwords.expenses.feature.expenses.ui.list

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.Serializable

@Serializable
object ExpensesPaneDestination : Destination

fun getExpensesPaneNavModule(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
): NavModule {
    return NavModule(ExpensesPaneDestination.serializer()) {
        entry<ExpensesPaneDestination> {
            val viewModel = viewModel<ExpensesViewModel>(factory = viewModelFactory {
                initializer {
                    ExpensesViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractor,
                        expensesInteractor = expensesInteractor,
                        settingsRepository = settingsRepository,
                    )
                }
            })
            ExpensesPane(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onMenuClick = viewModel::onMenuClick,
                onAddExpenseClick = viewModel::onAddExpenseClick,
                onRevertExpenseClick = viewModel::onRevertExpenseClick,
                onDebtsDetailsClick = viewModel::onDebtsDetailsClick,
                onReplenishmentClick = viewModel::onReplenishmentClick,
                onCreateEventClick = viewModel::onCreateEventClick,
                onJoinEventClick = viewModel::onJoinEventClick,
                onJoinLocalEventClick = viewModel::onJoinLocalEventClick,
                onRefresh = viewModel::onRefresh,
            )
        }
    }
}
