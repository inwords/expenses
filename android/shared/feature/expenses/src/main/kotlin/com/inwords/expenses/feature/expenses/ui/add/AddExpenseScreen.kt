package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.ExpenseSplitWithPersonUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun AddExpenseScreen(
    state: SimpleScreenState<AddExpenseScreenUiModel>,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (PersonInfoUiModel) -> Unit,
    onSubjectPersonClicked: (PersonInfoUiModel) -> Unit,
    onEqualSplitChange: (Boolean) -> Unit,
    onWholeAmountChanged: (String) -> Unit,
    onSplitAmountChanged: (ExpenseSplitWithPersonUiModel, String) -> Unit,
    onConfirmClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> AddExpenseScreenSuccess(
            state = state.data,
            onCurrencyClicked = onCurrencyClicked,
            onExpenseTypeClicked = onExpenseTypeClicked,
            onPersonClicked = onPersonClicked,
            onSubjectPersonClicked = onSubjectPersonClicked,
            onEqualSplitChange = onEqualSplitChange,
            onWholeAmountChanged = onWholeAmountChanged,
            onSplitAmountChanged = onSplitAmountChanged,
            onConfirmClicked = onConfirmClicked,
            onCloseClicked = onCloseClicked,
            modifier = modifier
        )

        is SimpleScreenState.Loading -> {
            Text(text = "Loading")
        }

        is SimpleScreenState.Error -> {
            Text(text = "Error")
        }

        SimpleScreenState.Empty -> {
            Text(text = "No expenses")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseScreenSuccess(
    state: AddExpenseScreenUiModel,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (PersonInfoUiModel) -> Unit,
    onSubjectPersonClicked: (PersonInfoUiModel) -> Unit,
    onEqualSplitChange: (Boolean) -> Unit,
    onWholeAmountChanged: (String) -> Unit,
    onSplitAmountChanged: (ExpenseSplitWithPersonUiModel, String) -> Unit,
    onConfirmClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Операция")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onCloseClicked
                    ) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = "Оплатил",
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.persons.forEach { person ->
                    InputChip(
                        selected = person.selected,
                        onClick = { onPersonClicked.invoke(person) },
                        label = { Text(text = person.personName) }
                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                text = "Валюта",
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.currencies.forEach { currencyInfo ->
                    InputChip(
                        selected = currencyInfo.selected,
                        onClick = { onCurrencyClicked.invoke(currencyInfo) },
                        label = { Text(text = currencyInfo.currencyName) }
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                text = "Тип",
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputChip(
                    selected = state.expenseType == ExpenseType.Spending,
                    onClick = { onExpenseTypeClicked.invoke(ExpenseType.Spending) },
                    label = { Text(text = "Трата") }
                )
                InputChip(
                    selected = state.expenseType == ExpenseType.Replenishment,
                    onClick = { onExpenseTypeClicked.invoke(ExpenseType.Replenishment) },
                    label = { Text(text = "Возврат") }
                )
            }

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                text = "Разделить",
                style = MaterialTheme.typography.headlineMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    text = "Поровну",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = state.equalSplit,
                    onCheckedChange = onEqualSplitChange,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = "между:",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.subjectPersons.forEach { person ->
                    InputChip(
                        selected = person.selected,
                        onClick = { onSubjectPersonClicked.invoke(person) },
                        label = { Text(text = person.personName) }
                    )
                }
            }

            if (state.equalSplit) {
                val keyboardController = LocalSoftwareKeyboardController.current

                SplitEqualPartsInput(
                    amount = state.wholeAmount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onAmountChanged = {
                        onWholeAmountChanged.invoke(it)
                    },
                    onDoneClicked = { keyboardController?.hide() }
                )
            } else {
                SplitCustomPartsInputsBLock(
                    split = state.split,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onSplitAmountChanged = onSplitAmountChanged,
                    onDoneClicked = onConfirmClicked
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(all = 16.dp),
                onClick = onConfirmClicked,
            ) {
                Text(text = "Подтвердить")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun SplitEqualPartsInput(
    amount: String,
    onAmountChanged: (String) -> Unit,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.headlineSmall,
        value = amount,
        label = { Text(text = "Общая сумма") },
        onValueChange = onAmountChanged,
        keyboardActions = KeyboardActions(onDone = { onDoneClicked.invoke() }),
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SplitCustomPartsInputsBLock(
    split: ImmutableList<ExpenseSplitWithPersonUiModel>,
    onSplitAmountChanged: (ExpenseSplitWithPersonUiModel, String) -> Unit,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        split.forEach { expenseSplit ->
            SplitCustomPartInput(
                modifier = Modifier
                    .fillMaxWidth(),
                expenseSplit = expenseSplit,
                onAmountChanged = { onSplitAmountChanged(expenseSplit, it) },
                onDoneClicked = onDoneClicked
            )
        }
    }
}

@Composable
private fun SplitCustomPartInput(
    expenseSplit: ExpenseSplitWithPersonUiModel,
    onAmountChanged: (String) -> Unit,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.headlineSmall,
        value = expenseSplit.amount,
        label = { Text(text = expenseSplit.person.personName) },
        onValueChange = onAmountChanged,
        keyboardActions = KeyboardActions(onDone = { onDoneClicked.invoke() }),
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpensesScreenSuccessPreview() {
    AddExpenseScreenSuccess(
        onCurrencyClicked = {},
        onExpenseTypeClicked = {},
        onPersonClicked = {},
        onSubjectPersonClicked = {},
        onEqualSplitChange = {},
        onWholeAmountChanged = {},
        onSplitAmountChanged = { _, _ -> },
        onConfirmClicked = {},
        onCloseClicked = {},
        state = mockAddExpenseScreenUiModel(),
    )
}

internal fun mockAddExpenseScreenUiModel(): AddExpenseScreenUiModel {
    val person1 = Person(
        id = 1,
        name = "Василий"
    )
    val person2 = Person(
        id = 2,
        name = "Максим"
    )
    val person3 = Person(
        id = 3,
        name = "Анжела"
    )
    val person4 = Person(
        id = 4,
        name = "Саша"
    )
    return AddExpenseScreenUiModel(
        currencies = persistentListOf(
            CurrencyInfoUiModel(
                currencyCode = "USD",
                currencyName = "US Dollar",
                selected = true
            ),
            CurrencyInfoUiModel(
                currencyCode = "EUR",
                currencyName = "Euro",
                selected = false
            ),
            CurrencyInfoUiModel(
                currencyCode = "RUB",
                currencyName = "Russian Ruble",
                selected = false
            ),
        ),
        expenseType = ExpenseType.Spending,
        persons = persistentListOf(
            PersonInfoUiModel(
                personId = person1.id,
                personName = person1.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person2.id,
                personName = person2.name,
                selected = false
            ),
            PersonInfoUiModel(
                personId = person3.id,
                personName = person3.name,
                selected = false
            ),
            PersonInfoUiModel(
                personId = person4.id,
                personName = person4.name,
                selected = false
            ),
        ),
        subjectPersons = persistentListOf(
            PersonInfoUiModel(
                personId = person1.id,
                personName = person1.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person2.id,
                personName = person2.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person3.id,
                personName = person3.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person4.id,
                personName = person4.name,
                selected = true
            ),
        ),
        equalSplit = false,
        wholeAmount = "",
        split = persistentListOf(
            ExpenseSplitWithPersonUiModel(
                person = PersonInfoUiModel(
                    personId = person1.id,
                    personName = person1.name,
                    selected = true
                ),
                amount = "33"
            ),
            ExpenseSplitWithPersonUiModel(
                person = PersonInfoUiModel(
                    personId = person2.id,
                    personName = person2.name,
                    selected = true
                ),
                amount = "34"
            ),
        )
    )
}
