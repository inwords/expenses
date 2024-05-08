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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.ExpenseUiModel
import kotlinx.datetime.Clock

@Composable
internal fun ExpensesScreen(
    modifier: Modifier = Modifier,
    onAddExpenseClick: () -> Unit,
    onHomeClick: () -> Unit,
    state: SimpleScreenState<ExpensesScreenUiModel>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is SimpleScreenState.Success -> ExpensesScreenSuccess(
                modifier = modifier,
                onHomeClick = onHomeClick,
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
            Text(text = "Добавить расход")
        }
    }
}

@Composable
internal fun ExpensesScreenSuccess(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    state: ExpensesScreenUiModel
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 32.dp
                ),
            onClick = onHomeClick
        ) {
            Text(text = "Home")
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp),
            reverseLayout = true
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
                            text = "$amountSign${expense.amount.toPlainString()}",
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
        onHomeClick = {},
        state = SimpleScreenState.Success(mockExpensesScreenUiModel())
    )
}

internal fun mockExpensesScreenUiModel(): ExpensesScreenUiModel {
    return ExpensesScreenUiModel(
        expenses = listOf(
            ExpenseUiModel(
                expense = Expense(
                    expenseId = 1,
                    amount = 100.toBigDecimal(),
                    currency = Currency(
                        id = 1,
                        code = "RUB",
                        name = "Russian Ruble",
                    ),
                    expenseType = ExpenseType.Spending,
                    person = Person(
                        id = 1,
                        name = "Василий"
                    ),
                    subjectPersons = listOf(
                        Person(
                            id = 2,
                            name = "Максим"
                        )
                    ),
                    timestamp = Clock.System.now(),
                    description = "Lunch",
                )
            ),
            ExpenseUiModel(
                expense = Expense(
                    expenseId = 2,
                    amount = 202322320.toBigDecimal(),
                    currency = Currency(
                        id = 2,
                        code = "USD",
                        name = "US Dollar",
                    ),
                    expenseType = ExpenseType.Replenishment,
                    person = Person(
                        id = 2,
                        name = "Максим"
                    ),
                    subjectPersons = listOf(
                        Person(
                            id = 1,
                            name = "Василий"
                        )
                    ),
                    timestamp = Clock.System.now(),
                    description = "Dinner and some text",
                )
            )
        )
    )
}
