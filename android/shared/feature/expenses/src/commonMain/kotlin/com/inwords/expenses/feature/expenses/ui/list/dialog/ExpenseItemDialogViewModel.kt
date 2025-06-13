package com.inwords.expenses.feature.expenses.ui.list.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class ExpenseItemDialogViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    private val expenseId: Long,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    fun onRevertExpenseClick() {
        val event = eventsInteractor.currentEvent.value?.event ?: return

        // no need to cancel this operation
        viewModelScope.launch {
            expensesInteractor.revertExpense(event = event, expenseId = expenseId)

            navigationController.popBackStack(
                toDestination = ExpensesScreenDestination,
                inclusive = false
            )
        }
    }

}
