package com.inwords.expenses.feature.expenses.ui.list.dialog.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.DefaultStringProvider
import com.inwords.expenses.core.ui.utils.StringProvider
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneDestination
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_revert_description
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class ExpenseItemDialogViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    private val expensesLocalStore: ExpensesLocalStore,
    private val expenseId: Long,
    private val stringProvider: StringProvider = DefaultStringProvider,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var expenseRevertJob: Job? = null

    fun onRevertExpenseClick() {
        val event = eventsInteractor.currentEvent.value?.event ?: return

        if (expenseRevertJob != null) return // no need to cancel this operation
        expenseRevertJob = viewModelScope.launch {
            val originalExpense = expensesLocalStore.getExpense(expenseId) ?: return@launch
            expensesInteractor.revertExpense(
                event = event,
                originalExpense = originalExpense,
                description = stringProvider.getString(Res.string.expenses_revert_description, originalExpense.description)
            )

            navigationController.popBackStack(
                toDestination = ExpensesPaneDestination,
                inclusive = false
            )
        }
    }

}
