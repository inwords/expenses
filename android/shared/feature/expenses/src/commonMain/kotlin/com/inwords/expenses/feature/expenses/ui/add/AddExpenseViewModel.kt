package com.inwords.expenses.feature.expenses.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.ui.utils.updateIfSuccess
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.divide
import com.inwords.expenses.core.utils.toBigDecimalOrNull
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.domain.model.PersonWithAmount
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination.Replenishment
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.ExpenseSplitWithPersonUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseViewModel.AddExpenseScreenModel.AmountModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseViewModel.AddExpenseScreenModel.ExpenseSplitWithPersonModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseViewModel.AddExpenseScreenModel.PersonInfoModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import com.inwords.expenses.feature.settings.api.SettingsRepository
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class AddExpenseViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
    private val replenishment: Replenishment?,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private data class AddExpenseScreenModel(
        val event: Event,
        val description: String,
        val currencies: List<CurrencyInfoModel>,
        val expenseType: ExpenseType,
        val persons: List<PersonInfoModel>,
        val subjectPersons: List<PersonInfoModel>,
        val equalSplit: Boolean,
        val wholeAmount: AmountModel,
        val split: List<ExpenseSplitWithPersonModel>,
    ) {

        data class CurrencyInfoModel(
            val currency: Currency,
            val selected: Boolean,
        )

        data class PersonInfoModel(
            val person: Person,
            val selected: Boolean,
        )

        data class ExpenseSplitWithPersonModel(
            val person: PersonInfoModel,
            val amount: AmountModel,
        )

        data class AmountModel(
            val amount: BigDecimal?,
            val amountRaw: String,
        )

    }

    private val _state = MutableStateFlow<SimpleScreenState<AddExpenseScreenModel>>(SimpleScreenState.Empty)

    val state: StateFlow<SimpleScreenState<AddExpenseScreenUiModel>> = _state
        .map { state ->
            when (state) {
                SimpleScreenState.Empty -> SimpleScreenState.Empty
                SimpleScreenState.Error -> SimpleScreenState.Error
                SimpleScreenState.Loading -> SimpleScreenState.Loading
                is SimpleScreenState.Success -> SimpleScreenState.Success(state.data.toUiModel())
            }
        }
        .stateIn(viewModelScope + UI, started = SharingStarted.Eagerly, initialValue = SimpleScreenState.Empty)

    init {
        combine(
            eventsInteractor.currentEvent
                .filterNotNull(), // TODO mvp
            settingsRepository.getCurrentPersonId()
        ) { eventDetails, currentPersonId ->
            val subjectPersons = eventDetails.persons.map { person ->
                PersonInfoModel(
                    person = person,
                    selected = if (replenishment == null) {
                        true
                    } else {
                        person.id == replenishment.toPersonId
                    }
                )
            }

            val state = (_state.value as? SimpleScreenState.Success<AddExpenseScreenModel>)?.data
            val selectedCurrency = if (replenishment == null) {
                val selectedCurrencyFromState = state?.currencies?.firstOrNull { it.selected }?.currency
                selectedCurrencyFromState?.let {
                    eventDetails.currencies.firstOrNull { it.code == selectedCurrencyFromState.code }
                } ?: eventDetails.primaryCurrency
            } else {
                eventDetails.currencies.firstOrNull { it.code == replenishment.currencyCode }
                    ?: eventDetails.primaryCurrency
            }
            AddExpenseScreenModel(
                event = eventDetails.event,
                description = if (replenishment == null) {
                    state?.description.orEmpty()
                } else {
                    val currentPerson = eventDetails.persons.first { it.id == replenishment.fromPersonId }
                    "Возврат от ${currentPerson.name}"
                },
                currencies = eventDetails.currencies.map { currency ->
                    AddExpenseScreenModel.CurrencyInfoModel(
                        currency = currency,
                        selected = currency == selectedCurrency,
                    )
                },
                expenseType = replenishment?.let { ExpenseType.Replenishment } ?: state?.expenseType ?: ExpenseType.Spending,
                persons = eventDetails.persons.map { person ->
                    PersonInfoModel(
                        person = person,
                        selected = if (replenishment == null) {
                            person.id == currentPersonId
                        } else {
                            person.id == replenishment.fromPersonId
                        }
                    )
                },
                subjectPersons = subjectPersons,
                equalSplit = replenishment == null,
                wholeAmount = state?.wholeAmount ?: AmountModel(
                    amount = null,
                    amountRaw = ""
                ),
                split = if (replenishment == null) {
                    emptyList()
                } else {
                    listOf(
                        ExpenseSplitWithPersonModel(
                            person = subjectPersons.first { it.selected },
                            amount = AmountModel(
                                amount = replenishment.amount.toBigDecimalOrNull(),
                                amountRaw = replenishment.amount
                            )
                        )
                    )
                }
            )
        }
            .collectIn(viewModelScope) { screenUiModel ->
                // TODO mvp - needs error handling and user input preservation (mask)
                _state.value = SimpleScreenState.Success(screenUiModel)
            }
    }

    fun onExpenseTypeClicked(type: ExpenseType) {
        _state.updateIfSuccess { state ->
            state.copy(expenseType = type)
        }
    }

    fun onCurrencyClicked(currency: CurrencyInfoUiModel) {
        _state.updateIfSuccess { state ->
            state.copy(
                currencies = state.currencies.map { currencyUiModel ->
                    currencyUiModel.copy(selected = currency.currencyCode == currencyUiModel.currency.code)
                }
            )
        }
    }

    fun onPersonClicked(person: PersonInfoUiModel) {
        _state.updateIfSuccess { state ->
            val newSubjectPersons = state.persons.map {
                state.subjectPersons.firstOrNull { personUiModel -> personUiModel.person.id == it.person.id } ?: it
            }
            val newSplit = ensureSplitCalculated(
                equalSplit = state.equalSplit,
                wholeAmount = state.wholeAmount,
                split = state.split,
                subjectPersons = newSubjectPersons
            )
            state.copy(
                persons = state.persons.map { personUiModel ->
                    personUiModel.copy(selected = personUiModel.person.id == person.personId)
                },
                subjectPersons = newSubjectPersons,
                split = newSplit,
            )
        }
    }

    fun onSubjectPersonClicked(person: PersonInfoUiModel) {
        _state.updateIfSuccess { state ->
            val newSubjectPersons = state.subjectPersons.map { personUiModel ->
                if (personUiModel.person.id == person.personId) {
                    personUiModel.copy(selected = !personUiModel.selected)
                } else {
                    personUiModel
                }
            }

            val newSplit = ensureSplitCalculated(
                equalSplit = state.equalSplit,
                wholeAmount = state.wholeAmount,
                split = state.split,
                subjectPersons = newSubjectPersons
            )

            state.copy(
                subjectPersons = newSubjectPersons,
                split = newSplit,
            )
        }
    }

    fun onEqualSplitChange(equalSplit: Boolean) {
        _state.updateIfSuccess { state ->
            val newSplit = ensureSplitCalculated(
                equalSplit = equalSplit,
                wholeAmount = state.wholeAmount,
                split = state.split,
                subjectPersons = state.subjectPersons
            )

            state.copy(
                equalSplit = equalSplit,
                split = newSplit,
            )
        }
    }

    private fun ensureSplitCalculated(
        equalSplit: Boolean,
        wholeAmount: AmountModel?,
        split: List<ExpenseSplitWithPersonModel>,
        subjectPersons: List<PersonInfoModel>,
    ): List<ExpenseSplitWithPersonModel> {
        return if (equalSplit) {
            split
        } else {
            val selectedSubjectPersons = subjectPersons.filter { it.selected }

            val newSplit = split.ifEmpty {
                val amount = wholeAmount?.amount?.divide(
                    other = selectedSubjectPersons.size.coerceAtLeast(1).toBigDecimal(),
                    scale = 2,
                )
                selectedSubjectPersons.map { personInfoModel ->
                    ExpenseSplitWithPersonModel(
                        person = personInfoModel,
                        amount = AmountModel(amount, amount?.toRoundedString(2).orEmpty())
                    )
                }
            }

            if (newSplit.map { it.person } == selectedSubjectPersons) {
                newSplit
            } else {
                selectedSubjectPersons.map { personInfoModel ->
                    newSplit.firstOrNull { it.person.person.id == personInfoModel.person.id } ?: ExpenseSplitWithPersonModel(
                        person = personInfoModel,
                        amount = AmountModel(null, ""),
                    )
                }
            }
        }
    }

    fun onWholeAmountChanged(amount: String) {
        val newAmount = amount.parseToAmountModel()

        _state.updateIfSuccess { state ->
            state.copy(wholeAmount = newAmount)
        }
    }

    fun onSplitAmountChanged(person: ExpenseSplitWithPersonUiModel, amount: String) {
        val newAmount = amount.parseToAmountModel()

        _state.updateIfSuccess { state ->
            state.copy(
                split = state.split.map { split ->
                    if (split.person.person.id == person.person.personId) {
                        split.copy(amount = newAmount)
                    } else {
                        split
                    }
                }
            )
        }
    }

    private fun String.parseToAmountModel(): AmountModel {
        val trimmedAmount = this.trim()
        return AmountModel(
            amount = trimmedAmount.toBigDecimalOrNull(),
            amountRaw = trimmedAmount
        )
    }

    fun onDescriptionChanged(description: String) {
        _state.updateIfSuccess { state ->
            state.copy(description = description)
        }
    }

    fun onConfirmClicked() {
        val state = (_state.value as? SimpleScreenState.Success)?.data ?: return

        viewModelScope.launch {
            val selectedCurrency = state.currencies.first { it.selected }.currency
            val selectedPerson = state.persons.first { it.selected }.person
            if (state.equalSplit) {
                expensesInteractor.addExpenseEqualSplit(
                    event = state.event,
                    wholeAmount = state.wholeAmount.amount ?: return@launch,
                    expenseType = state.expenseType,
                    description = state.description,
                    selectedSubjectPersons = state.subjectPersons.filter { it.selected }.map { it.person },
                    selectedCurrency = selectedCurrency,
                    selectedPerson = selectedPerson,
                )
            } else {
                val personWithAmountSplit = state.split.map {
                    PersonWithAmount(it.person.person, it.amount.amount ?: return@launch)
                }
                expensesInteractor.addExpenseCustomSplit(
                    event = state.event,
                    expenseType = state.expenseType,
                    description = state.description,
                    selectedCurrency = selectedCurrency,
                    selectedPerson = selectedPerson,
                    personWithAmountSplit = personWithAmountSplit
                )
            }

            navigationController.popBackStack()
        }
    }

    fun onCloseClicked() {
        navigationController.popBackStack()
    }

    private fun AddExpenseScreenModel.toUiModel(): AddExpenseScreenUiModel {
        return AddExpenseScreenUiModel(
            description = this.description,
            currencies = this.currencies.map { currencyInfoModel ->
                CurrencyInfoUiModel(
                    currencyName = currencyInfoModel.currency.name,
                    currencyCode = currencyInfoModel.currency.code,
                    selected = currencyInfoModel.selected
                )
            }.asImmutableListAdapter(),
            expenseType = this.expenseType,
            persons = this.persons.map { personInfoModel ->
                PersonInfoUiModel(
                    personId = personInfoModel.person.id,
                    personName = personInfoModel.person.name,
                    selected = personInfoModel.selected
                )
            }.asImmutableListAdapter(),
            subjectPersons = this.subjectPersons.map { personInfoModel ->
                PersonInfoUiModel(
                    personId = personInfoModel.person.id,
                    personName = personInfoModel.person.name,
                    selected = personInfoModel.selected
                )
            }.asImmutableListAdapter(),
            equalSplit = this.equalSplit,
            wholeAmount = this.wholeAmount.amountRaw,
            split = this.split.map { expenseSplitWithPersonModel ->
                ExpenseSplitWithPersonUiModel(
                    person = PersonInfoUiModel(
                        personId = expenseSplitWithPersonModel.person.person.id,
                        personName = expenseSplitWithPersonModel.person.person.name,
                        selected = expenseSplitWithPersonModel.person.selected
                    ),
                    amount = expenseSplitWithPersonModel.amount.amountRaw,
                )
            }.asImmutableListAdapter()
        )
    }

}
