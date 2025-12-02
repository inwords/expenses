package com.inwords.expenses.feature.expenses.ui.list.dialog.item

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import kotlinx.serialization.Serializable

@Serializable
internal data class ExpenseItemDialogDestination(
    val expenseId: Long,
    val description: String,
) : Destination

fun getExpenseItemDialogNavModule(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    expensesLocalStore: ExpensesLocalStore,
): NavModule {
    return NavModule(ExpenseItemDialogDestination.serializer()) {
        entry<ExpenseItemDialogDestination>(metadata = dialog()) { key ->
            val viewModel = viewModel<ExpenseItemDialogViewModel>(factory = viewModelFactory {
                initializer {
                    ExpenseItemDialogViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractor,
                        expensesInteractor = expensesInteractor,
                        expensesLocalStore = expensesLocalStore,
                        expenseId = key.expenseId
                    )
                }
            })
            ExpenseItemDialog(
                state = ExpenseItemDialogUiModel(
                    description = key.description
                ),
                onRevertExpenseClick = viewModel::onRevertExpenseClick,
            )
        }
    }
}
