package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import kotlinx.serialization.Serializable

@Serializable
object DebtsListScreenDestination : Destination

fun NavGraphBuilder.debtsListScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
) {
    composable<DebtsListScreenDestination> {
        val viewModel = viewModel<DebtsListViewModel>(it, factory = viewModelFactory {
            initializer {
                DebtsListViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesInteractor = expensesInteractor,
                )
            }
        })
        DebtsListScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onReplenishmentClick = viewModel::onReplenishmentClick,
            onCloseClick = viewModel::onCloseClick
        )
    }
}