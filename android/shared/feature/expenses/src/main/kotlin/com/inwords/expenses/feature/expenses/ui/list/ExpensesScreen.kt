package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.ExpenseUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import kotlinx.datetime.Clock

@Composable
internal fun ExpensesScreen(
    modifier: Modifier = Modifier,
    onAddExpenseClick: () -> Unit,
    state: SimpleScreenState<ExpensesScreenUiModel>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is SimpleScreenState.Success -> ExpensesScreenSuccess(
                modifier = modifier,
                state = state.data,
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

        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onAddExpenseClick
        ) {
            Icon(Icons.Outlined.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Операция")
        }
    }
}

@Composable
internal fun ExpensesScreenSuccess(
    modifier: Modifier = Modifier,
    state: ExpensesScreenUiModel
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Долги",
                style = MaterialTheme.typography.headlineMedium
            )
            TextButton(
                onClick = { /*TODO*/ } // TODO
            ) {
                Text(
                    text = "детализация",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
            }
        }

        state.creditors.forEach { creditor ->
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                onClick = { /*TODO*/ } // TODO
            ) {
                Text(
                    text = "${creditor.amount.toRoundedString()},  ${creditor.person.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
            text = "Операции",
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp),
        ) {
            items(count = state.expenses.size,
                key = {
                    state.expenses[state.expenses.lastIndex - it].expense.expenseId
                }
            ) { index ->
                val shape = RoundedCornerShape(8.dp)
                val expense = state.expenses[state.expenses.lastIndex - index].expense
                Row(
                    modifier = Modifier
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
                        val amountSign = when (expense.expenseType) {
                            ExpenseType.Spending -> ""
                            ExpenseType.Replenishment -> "+"
                        }
                        Text(
                            text = "$amountSign${expense.totalAmount.toRoundedString()}",
                            style = MaterialTheme.typography.displaySmall,
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
                                text = expense.currency.name,
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
                        Text(text = expense.person.name, maxLines = 1)
                        Text(text = expense.timestamp.toString().take(10), maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ExpensesScreenPreview() {
    ExpensesScreen(
        onAddExpenseClick = {},
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
        creditors = listOf(
            ExpensesScreenUiModel.DebtorShortUiModel(
                person = person1,
                amount = 100.toBigDecimal()
            ),
            ExpensesScreenUiModel.DebtorShortUiModel(
                person = person2,
                amount = 150.toBigDecimal()
            )
        ),
        expenses = listOf(
            ExpenseUiModel(
                expense = Expense(
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
                            amount = 150.toBigDecimal()
                        )
                    ),
                    timestamp = Clock.System.now(),
                    description = "Lunch",
                )
            ),
            ExpenseUiModel(
                expense = Expense(
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
                )
            )
        )
    )
}
