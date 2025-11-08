package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneDestination
import kotlinx.serialization.Serializable

@Serializable
object DebtsListPaneDestination : Destination

fun getDebtsListPaneNavModule(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
): NavModule {
    return NavModule(AddExpensePaneDestination.serializer()) {
        entry<DebtsListPaneDestination> {
            val viewModel = viewModel<DebtsListViewModel>(factory = viewModelFactory {
                initializer {
                    DebtsListViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractor,
                        expensesInteractor = expensesInteractor,
                    )
                }
            })
            DebtsListPane(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onReplenishmentClick = viewModel::onReplenishmentClick,
                onCloseClick = viewModel::onCloseClick
            )
        }
    }
}
