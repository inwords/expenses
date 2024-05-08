package com.inwords.expenses.feature.expenses.ui.add

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
import kotlinx.serialization.Serializable

@Serializable
object AddExpenseScreenDestination : Destination

fun NavGraphBuilder.addExpenseScreen(
    navigationController: DefaultNavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    homeScreenDestination: Destination,
) {
    composable<AddExpenseScreenDestination> {
        val viewModel = viewModel<AddExpenseViewModel>(it, factory = viewModelFactory {
            initializer {
                AddExpenseViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesInteractor = expensesInteractor,
                    homeScreenDestination = homeScreenDestination,
                )
            }
        })
        AddExpenseScreen(
            onHomeClicked = viewModel::onHomeClicked,
            onAmountChanged = viewModel::onAmountChanged,
            onCurrencyClicked = viewModel::onCurrencyClicked,
            onExpenseTypeClicked = viewModel::onExpenseTypeClicked,
            onPersonClicked = viewModel::onPersonClicked,
            onSubjectPersonClicked = viewModel::onSubjectPersonClicked,
            onConfirmClicked = viewModel::onConfirmClicked,
            state = viewModel.state.collectAsStateWithLifecycle().value,
        )
    }
}