package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.ExpenseUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock

@Composable
internal fun ExpensesScreen(
    state: SimpleScreenState<ExpensesScreenUiModel>,
    onAddExpenseClick: () -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (ExpensesScreenUiModel.DebtorShortUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> ExpensesScreenSuccess(
            state = state.data,
            onAddExpenseClick = onAddExpenseClick,
            onDebtsDetailsClick = onDebtsDetailsClick,
            onReplenishmentClick = onReplenishmentClick,
            modifier = modifier,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun ExpensesScreenSuccess(
    state: ExpensesScreenUiModel,
    onAddExpenseClick: () -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (ExpensesScreenUiModel.DebtorShortUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                            append("Expenses")
                        }
                        append("  ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                            append(state.currentPersonName)
                        }
                    }
                    Text(text = text)
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddExpenseClick
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Операция")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Долги",
                    style = MaterialTheme.typography.headlineMedium
                )
                TextButton(
                    onClick = onDebtsDetailsClick
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = "детализация",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                }
            }

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.creditors.forEach { creditor ->
                    ReturnCreditorDebtButton(creditor, { onReplenishmentClick.invoke(creditor) })
                }
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                text = "Операции",
                style = MaterialTheme.typography.headlineMedium
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
            ) {
                items(
                    count = state.expenses.size,
                    key = { index ->
                        state.expenses[state.expenses.lastIndex - index].expenseId
                    }
                ) { index ->
                    val expense = state.expenses[state.expenses.lastIndex - index]
                    ExpenseItem(expense)
                }
            }
        }
    }
}

@Composable
private fun ReturnCreditorDebtButton(
    creditor: ExpensesScreenUiModel.DebtorShortUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = "${creditor.amount} ${creditor.currencyName},  ${creditor.personName}",
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
    }
}

@Composable
private fun ExpenseItem(
    expense: ExpenseUiModel,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(elevation = 4.dp, shape = shape)
            .background(color = MaterialTheme.colorScheme.background, shape = shape),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = false)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val amountColor = when (expense.expenseType) {
                ExpenseType.Spending -> MaterialTheme.colorScheme.onBackground
                ExpenseType.Replenishment -> MaterialTheme.colorScheme.primary
            }
            Text(
                text = expense.totalAmount,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                color = amountColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = expense.currencyName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = expense.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
        ) {
            Text(text = expense.personName, maxLines = 1)
            Text(text = expense.timestamp, maxLines = 1)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ExpensesScreenPreview() {
    ExpensesScreen(
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        state = SimpleScreenState.Success(mockExpensesScreenUiModel())
    )
}

internal fun mockExpensesScreenUiModel(): ExpensesScreenUiModel {
    val person1 = Person(
        id = 1,
        name = "Василий"
    )
    val person2 = Person(
        id = 2,
        name = "Максим"
    )
    return ExpensesScreenUiModel(
        currentPersonId = person1.id,
        currentPersonName = person1.name,
        creditors = persistentListOf(
            ExpensesScreenUiModel.DebtorShortUiModel(
                personId = person1.id,
                personName = person1.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "100"
            ),
            ExpensesScreenUiModel.DebtorShortUiModel(
                personId = person2.id,
                personName = person2.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "150"
            )
        ),
        expenses = persistentListOf(
            Expense(
                expenseId = 1,
                currency = Currency(
                    id = 1,
                    code = "RUB",
                    name = "Russian Ruble",
                ),
                expenseType = ExpenseType.Spending,
                person = person1,
                subjecExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 1,
                        expenseId = 1,
                        person = person1,
                        amount = 100.toBigDecimal()
                    ),
                    ExpenseSplitWithPerson(
                        expenseSplitId = 2,
                        expenseId = 1,
                        person = person2,
                        amount = 150.333.toBigDecimal()
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Lunch",
            ).toUiModel(),
            Expense(
                expenseId = 2,
                currency = Currency(
                    id = 2,
                    code = "USD",
                    name = "US Dollar",
                ),
                expenseType = ExpenseType.Replenishment,
                person = person2,
                subjecExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 4,
                        expenseId = 2,
                        person = person2,
                        amount = 132423423.toBigDecimal()
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Dinner and some text",
            ).toUiModel()
        )
    )
}
