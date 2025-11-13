package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.button.ButtonWithIconAndText
import com.inwords.expenses.core.ui.design.group.MultiSelectConnectedButtonGroupWithFlowLayout
import com.inwords.expenses.core.ui.design.group.ToggleButtonOption
import com.inwords.expenses.core.ui.design.loading.DefaultProgressIndicator
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.ExpenseSplitWithPersonUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.PersonInfoUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun AddExpensePane(
    state: SimpleScreenState<AddExpensePaneUiModel>,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (PersonInfoUiModel) -> Unit,
    onSubjectPersonClicked: (PersonInfoUiModel) -> Unit,
    onEqualSplitChange: (Boolean) -> Unit,
    onWholeAmountChanged: (String) -> Unit,
    onSplitAmountChanged: (ExpenseSplitWithPersonUiModel, String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> AddExpensePaneSuccess(
            state = state.data,
            onCurrencyClicked = onCurrencyClicked,
            onExpenseTypeClicked = onExpenseTypeClicked,
            onPersonClicked = onPersonClicked,
            onSubjectPersonClicked = onSubjectPersonClicked,
            onEqualSplitChange = onEqualSplitChange,
            onWholeAmountChanged = onWholeAmountChanged,
            onSplitAmountChanged = onSplitAmountChanged,
            onDescriptionChanged = onDescriptionChanged,
            onConfirmClicked = onConfirmClicked,
            modifier = modifier
        )

        is SimpleScreenState.Loading -> AddExpensePaneLoading()

        is SimpleScreenState.Error -> {
            Text(text = "Error")
        }

        SimpleScreenState.Empty -> {
            Text(text = "No expenses")
        }
    }
}

@Composable
private fun AddExpensePaneLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        DefaultProgressIndicator()
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddExpensePaneSuccess(
    state: AddExpensePaneUiModel,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (PersonInfoUiModel) -> Unit,
    onSubjectPersonClicked: (PersonInfoUiModel) -> Unit,
    onEqualSplitChange: (Boolean) -> Unit,
    onWholeAmountChanged: (String) -> Unit,
    onSplitAmountChanged: (ExpenseSplitWithPersonUiModel, String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = MaterialTheme.typography.headlineSmall,
            value = state.description,
            label = { Text(text = "Описание") },
            onValueChange = onDescriptionChanged,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            singleLine = true
        )

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 12.dp),
            text = "Оплатил",
            style = MaterialTheme.typography.headlineMedium
        )
        MultiSelectConnectedButtonGroupWithFlowLayout(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            options = state.persons.map { person ->
                ToggleButtonOption(
                    text = person.personName,
                    checked = person.selected,
                    payload = person
                )
            },
            onCheckedChange = { _, _, person -> onPersonClicked.invoke(person) },
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = "Валюта",
            style = MaterialTheme.typography.headlineMedium
        )
        MultiSelectConnectedButtonGroupWithFlowLayout(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            options = state.currencies.map { currencyInfo ->
                ToggleButtonOption(
                    text = currencyInfo.currencyName,
                    checked = currencyInfo.selected,
                    payload = currencyInfo
                )
            },
            onCheckedChange = { _, _, currencyInfo -> onCurrencyClicked.invoke(currencyInfo) },
        )

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = "Разделить",
            style = MaterialTheme.typography.headlineSmall
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
                    .testTag("equal_split_switch")
            )
            Text(
                modifier = Modifier
                    .padding(end = 8.dp),
                text = "между:",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        MultiSelectConnectedButtonGroupWithFlowLayout(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            options = state.subjectPersons.map { person ->
                ToggleButtonOption(
                    text = person.personName,
                    checked = person.selected,
                    payload = person
                )
            },
            onCheckedChange = { _, _, person -> onSubjectPersonClicked.invoke(person) },
        )

        if (state.equalSplit) {
            SplitEqualPartsInput(
                amount = state.wholeAmount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onAmountChanged = onWholeAmountChanged,
                onDoneClicked = { keyboardController?.hide() }
            )
        } else {
            SplitCustomPartsInputsBLock(
                split = state.split,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onSplitAmountChanged = onSplitAmountChanged,
                onDoneClicked = { keyboardController?.hide() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
        ) {
            TonalToggleButton(
                modifier = Modifier.weight(1f),
                checked = state.expenseType == ExpenseType.Spending,
                onCheckedChange = { onExpenseTypeClicked.invoke(ExpenseType.Spending) },
                shapes = ButtonGroupDefaults.connectedLeadingButtonShapes(),
            ) { Text(text = "Трата") }
            TonalToggleButton(
                modifier = Modifier.weight(1f),
                checked = state.expenseType == ExpenseType.Replenishment,
                onCheckedChange = { onExpenseTypeClicked.invoke(ExpenseType.Replenishment) },
                shapes = ButtonGroupDefaults.connectedTrailingButtonShapes(),
            ) { Text(text = "Возврат") }
        }

        ButtonWithIconAndText(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            onClick = onConfirmClicked,
            text = "Сохранить",
            imageVector = Icons.Outlined.Check,
            minHeight = ButtonDefaults.MediumContainerHeight,
        )
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        split.forEachIndexed { i, expenseSplit ->
            SplitCustomPartInput(
                modifier = Modifier
                    .fillMaxWidth(),
                expenseSplit = expenseSplit,
                onAmountChanged = { onSplitAmountChanged(expenseSplit, it) },
                onDoneClicked = onDoneClicked,
                imeAction = if (i == split.lastIndex) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                },
            )
        }
    }
}

@Composable
private fun SplitCustomPartInput(
    expenseSplit: ExpenseSplitWithPersonUiModel,
    onAmountChanged: (String) -> Unit,
    onDoneClicked: () -> Unit,
    imeAction: ImeAction,
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
            imeAction = imeAction,
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
private fun AddExpensePaneSuccessEqualSplitPreview() {
    ExpensesTheme {
        AddExpensePane(
            onCurrencyClicked = {},
            onExpenseTypeClicked = {},
            onPersonClicked = {},
            onSubjectPersonClicked = {},
            onEqualSplitChange = {},
            onDescriptionChanged = {},
            onWholeAmountChanged = {},
            onSplitAmountChanged = { _, _ -> },
            onConfirmClicked = {},
            state = SimpleScreenState.Success(mockAddExpenseScreenUiModel().copy(equalSplit = true)),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpensePaneSuccessPreview() {
    ExpensesTheme {
        AddExpensePane(
            onCurrencyClicked = {},
            onExpenseTypeClicked = {},
            onPersonClicked = {},
            onSubjectPersonClicked = {},
            onEqualSplitChange = {},
            onDescriptionChanged = {},
            onWholeAmountChanged = {},
            onSplitAmountChanged = { _, _ -> },
            onConfirmClicked = {},
            state = SimpleScreenState.Success(mockAddExpenseScreenUiModel()),
        )
    }
}

internal fun mockAddExpenseScreenUiModel(): AddExpensePaneUiModel {
    val person1 = Person(
        id = 1,
        serverId = "11",
        name = "Василий"
    )
    val person2 = Person(
        id = 2,
        serverId = "12",
        name = "Максим"
    )
    val person3 = Person(
        id = 3,
        serverId = "13",
        name = "Анжела"
    )
    val person4 = Person(
        id = 4,
        serverId = "14",
        name = "Саша"
    )
    return AddExpensePaneUiModel(
        description = "Чипсы и кола",
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
