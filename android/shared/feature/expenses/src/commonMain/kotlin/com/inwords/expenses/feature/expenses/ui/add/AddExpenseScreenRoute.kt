package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination.Replenishment
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class AddExpenseScreenDestination(
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

private class ReplenishmentNavType : NavType<Replenishment?>(
    isNullableAllowed = true
) {
    override fun get(bundle: SavedState, key: String): Replenishment? {
        return bundle.read {
            getStringOrNull(key)?.let { parseValue(it) }
        }
    }

    override fun parseValue(value: String): Replenishment? {
        return if (value.isEmpty()) {
            null
        } else {
            Json.decodeFromString(value)
        }
    }

    override fun put(bundle: SavedState, key: String, value: Replenishment?) {
        if (value != null) {
            bundle.write {
                putString(key, serializeAsValue(value))
            }
        }
    }

    override fun serializeAsValue(value: Replenishment?): String {
        return if (value == null) {
            ""
        } else {
            Json.encodeToString(value)
        }
    }
}

fun NavGraphBuilder.addExpenseScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) {
    composable<AddExpenseScreenDestination>(
        typeMap = mapOf(typeOf<Replenishment?>() to ReplenishmentNavType())
    ) { backStackEntry ->
        val destination = backStackEntry.toRoute<AddExpenseScreenDestination>()

        val viewModel = viewModel<AddExpenseViewModel>(backStackEntry, factory = viewModelFactory {
            initializer {
                AddExpenseViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesInteractor = expensesInteractor,
                    settingsRepository = settingsRepository,
                    replenishment = destination.replenishment,
                )
            }
        })
        AddExpenseScreen(
            state = viewModel.state.collectAsState().value, // FIXME: collectAsStateWithLifecycle has issues with Compose Navigation
            onCurrencyClicked = viewModel::onCurrencyClicked,
            onExpenseTypeClicked = viewModel::onExpenseTypeClicked,
            onPersonClicked = viewModel::onPersonClicked,
            onSubjectPersonClicked = viewModel::onSubjectPersonClicked,
            onEqualSplitChange = viewModel::onEqualSplitChange,
            onWholeAmountChanged = viewModel::onWholeAmountChanged,
            onSplitAmountChanged = viewModel::onSplitAmountChanged,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onConfirmClicked = viewModel::onConfirmClicked,
            onCloseClicked = viewModel::onCloseClicked,
        )
    }
}