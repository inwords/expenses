package com.inwords.expenses.feature.events.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.ui.add_persons.AddPersonsScreenDestination
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.events.ui.create.CreateEventViewModel.CreateEventScreenModel.CurrencyInfoModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class CreateEventViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private data class CreateEventScreenModel(
        val eventName: String,
        val currencies: List<CurrencyInfoModel>,
    ) {

        data class CurrencyInfoModel(
            val currency: Currency,
            val selected: Boolean,
        )
    }

    private var confirmJob: Job? = null

    private val _state = MutableStateFlow(CreateEventScreenModel("", emptyList())) // TODO use screen state
    val state: StateFlow<CreateEventScreenUiModel> = _state
        .map { state -> state.toUiModel() }
        .stateIn(viewModelScope + UI, started = SharingStarted.Eagerly, initialValue = CreateEventScreenUiModel("", persistentListOf()))

    init {
        eventsInteractor.getCurrencies()
            .collectIn(viewModelScope) { currencies ->
                _state.update { value ->
                    value.copy(
                        currencies = currencies.map { currency ->
                            currency.toUiModel(selected = currency.code == "RUB")
                        }
                    )
                }
            }
    }

    fun onEventNameChanged(eventName: String) {
        _state.update { value ->
            value.copy(eventName = eventName)
        }
    }

    fun onCurrencyClicked(currency: CurrencyInfoUiModel) {
        _state.update { state ->
            state.copy(
                currencies = state.currencies.map { currencyUiModel ->
                    currencyUiModel.copy(selected = currency.currencyCode == currencyUiModel.currency.code)
                }
            )
        }
    }

    fun onConfirmClicked() {
        // TODO mvp
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val state = _state.value
            eventsInteractor.draftEventName(
                eventName = state.eventName.takeIf { it.isNotBlank() } ?: return@launch
            )
            eventsInteractor.draftEventPrimaryCurrency(
                currency = state.currencies.first { it.selected }.currency
            )
            navigationController.navigateTo(
                destination = AddPersonsScreenDestination,
                popUpTo = expensesScreenDestination,
            )
        }
    }

    private fun Currency.toUiModel(selected: Boolean): CurrencyInfoModel {
        return CurrencyInfoModel(
            currency = this,
            selected = selected
        )
    }

    private fun CurrencyInfoModel.toUiModel(): CurrencyInfoUiModel {
        return CurrencyInfoUiModel(
            currencyName = currency.name,
            currencyCode = currency.code,
            selected = selected
        )
    }

    private fun CreateEventScreenModel.toUiModel(): CreateEventScreenUiModel {
        return CreateEventScreenUiModel(
            eventName = eventName,
            currencies = currencies.map { currency ->
                currency.toUiModel()
            }.asImmutableListAdapter()
        )
    }

}