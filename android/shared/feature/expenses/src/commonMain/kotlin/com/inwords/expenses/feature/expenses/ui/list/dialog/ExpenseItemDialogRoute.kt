package com.inwords.expenses.feature.expenses.ui.list.dialog

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import kotlinx.serialization.Serializable

@Serializable
internal data class ExpenseItemDialogDestination(
    val expenseId: Long,
    val description: String,
) : Destination

fun NavGraphBuilder.addExpenseItemDialog(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
) {
    dialog<ExpenseItemDialogDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<ExpenseItemDialogDestination>()

        val viewModel = viewModel<ExpenseItemDialogViewModel>(backStackEntry, factory = viewModelFactory {
            initializer {
                ExpenseItemDialogViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesInteractor = expensesInteractor,
                    expenseId = destination.expenseId
                )
            }
        })
        ExpenseItemDialog(
            state = ExpenseItemDialogUiModel(
                description = destination.description
            ),
            onRevertExpenseClick = viewModel::onRevertExpenseClick,
        )
    }
}