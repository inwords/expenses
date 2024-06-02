package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import java.math.BigDecimal

@Composable
internal fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    onHomeClicked: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencyClicked: (Currency) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (Person) -> Unit,
    onSubjectPersonClicked: (Person) -> Unit,
    onConfirmClicked: () -> Unit,
    state: SimpleScreenState<AddExpenseScreenUiModel>,
) {
    when (state) {
        is SimpleScreenState.Success -> AddExpenseScreenSuccess(
            modifier = modifier,
            onHomeClicked = onHomeClicked,
            onAmountChanged = onAmountChanged,
            onCurrencyClicked = onCurrencyClicked,
            onExpenseTypeClicked = onExpenseTypeClicked,
            onPersonClicked = onPersonClicked,
            onSubjectPersonClicked = onSubjectPersonClicked,
            onConfirmClicked = onConfirmClicked,
            state = state.data
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddExpenseScreenSuccess(
    modifier: Modifier = Modifier,
    onHomeClicked: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencyClicked: (Currency) -> Unit,
    onExpenseTypeClicked: (ExpenseType) -> Unit,
    onPersonClicked: (Person) -> Unit,
    onSubjectPersonClicked: (Person) -> Unit,
    onConfirmClicked: () -> Unit,
    state: AddExpenseScreenUiModel,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 32.dp
                ),
            onClick = onHomeClicked
        ) {
            Text(text = "Home")
        }

        TextField(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.displaySmall,
            value = state.amount?.toString().orEmpty(),
            label = { Text(text = "Сумма") },
            onValueChange = onAmountChanged,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            singleLine = true
        )

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.currencies.forEach { currencyInfo ->
                InputChip(
                    selected = currencyInfo.selected,
                    onClick = { onCurrencyClicked.invoke(currencyInfo.currency) },
                    label = { Text(text = currencyInfo.currency.name) }
                )
            }
        }

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

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.persons.forEach { person ->
                InputChip(
                    selected = person.selected,
                    onClick = { onPersonClicked.invoke(person.person) },
                    label = { Text(text = person.person.name) }
                )
            }
        }

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.subjectPersons.forEach { person ->
                InputChip(
                    selected = person.selected,
                    onClick = { onSubjectPersonClicked.invoke(person.person) },
                    label = { Text(text = person.person.name) }
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 8.dp),
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

@Composable
fun ButtonRound(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
    }
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondary
    }
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = CircleShape
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpensesScreenSuccessPreview() {
    AddExpenseScreenSuccess(
        onHomeClicked = {},
        onAmountChanged = {},
        onCurrencyClicked = {},
        onExpenseTypeClicked = {},
        onPersonClicked = {},
        onSubjectPersonClicked = {},
        onConfirmClicked = {},
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
        amount = BigDecimal.TEN,
        currencies = listOf(
            CurrencyInfoUiModel(
                currency = Currency(
                    id = 1,
                    code = "USD",
                    name = "US Dollar"
                ),
                selected = true
            ),
            CurrencyInfoUiModel(
                currency = Currency(
                    id = 2,
                    code = "EUR",
                    name = "Euro"
                ),
                selected = false
            ),
            CurrencyInfoUiModel(
                currency = Currency(
                    id = 3,
                    code = "RUB",
                    name = "Russian Ruble"
                ),
                selected = false
            ),
        ),
        expenseType = ExpenseType.Spending,
        persons = listOf(
            PersonInfoUiModel(
                person = person1,
                selected = true
            ),
            PersonInfoUiModel(
                person = person2,
                selected = false
            ),
            PersonInfoUiModel(
                person = person3,
                selected = false
            ),
            PersonInfoUiModel(
                person = person4,
                selected = false
            ),
        ),
        subjectPersons = listOf(
            PersonInfoUiModel(
                person = person2,
                selected = true
            ),
            PersonInfoUiModel(
                person = person3,
                selected = true
            ),
            PersonInfoUiModel(
                person = person4,
                selected = true
            ),
        ),
    )
}
