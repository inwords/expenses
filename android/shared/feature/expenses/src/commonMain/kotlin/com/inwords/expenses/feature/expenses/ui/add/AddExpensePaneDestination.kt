package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inwords.expenses.core.navigation.BottomSheetSceneStrategy.Companion.bottomSheet
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddExpensePaneDestination(
    @SerialName("replenishment")
    val replenishment: Replenishment? = null,
) : Destination {

    @Serializable
    data class Replenishment(
        @SerialName("fromPersonId")
        val fromPersonId: Long,
        @SerialName("toPersonId")
        val toPersonId: Long,
        @SerialName("currencyCode")
        val currencyCode: String,
        @SerialName("amount")
        val amount: String,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun getAddExpensePaneNavModule(
    navigationController: NavigationController,
    eventsInteractorLazy: Lazy<EventsInteractor>,
    expensesInteractorLazy: Lazy<ExpensesInteractor>,
    settingsRepositoryLazy: Lazy<SettingsRepository>,
): NavModule {
    return NavModule(AddExpensePaneDestination.serializer()) {
        entry<AddExpensePaneDestination>(metadata = bottomSheet()) { key ->
            val viewModel = viewModel<AddExpenseViewModel>(factory = viewModelFactory {
                initializer {
                    AddExpenseViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractorLazy.value,
                        expensesInteractor = expensesInteractorLazy.value,
                        settingsRepository = settingsRepositoryLazy.value,
                        replenishment = key.replenishment,
                    )
                }
            })
            AddExpensePane(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onCurrencyClicked = viewModel::onCurrencyClicked,
                onExpenseTypeClicked = viewModel::onExpenseTypeClicked,
                onPersonClicked = viewModel::onPersonClicked,
                onSubjectPersonClicked = viewModel::onSubjectPersonClicked,
                onEqualSplitChange = viewModel::onEqualSplitChange,
                onWholeAmountChanged = viewModel::onWholeAmountChanged,
                onSplitAmountChanged = viewModel::onSplitAmountChanged,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                onConfirmClicked = viewModel::onConfirmClicked,
            )
        }
    }
}
