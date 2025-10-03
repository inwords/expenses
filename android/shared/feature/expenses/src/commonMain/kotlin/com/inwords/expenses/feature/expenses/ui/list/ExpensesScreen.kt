package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.BasicTopAppBar
import com.inwords.expenses.core.ui.design.button.BasicFloatingActionButton
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.ui.common.EventInfoBlock
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.Expenses.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.Expenses.ExpenseUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.LocalEvents
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.LocalEvents.LocalEventUiModel
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
internal fun ExpensesScreen(
    state: SimpleScreenState<ExpensesScreenUiModel>,
    onMenuClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onRevertExpenseClick: (expense: ExpenseUiModel) -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (debtor: DebtorShortUiModel) -> Unit,
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> {
            when (val state = state.data) {
                is ExpensesScreenUiModel.Expenses -> ExpensesScreenSuccess(
                    state = state,
                    onMenuClick = onMenuClick,
                    onAddExpenseClick = onAddExpenseClick,
                    onRevertExpenseClick = onRevertExpenseClick,
                    onDebtsDetailsClick = onDebtsDetailsClick,
                    onReplenishmentClick = onReplenishmentClick,
                    onRefresh = onRefresh,
                    modifier = modifier
                )

                is LocalEvents -> ExpensesScreenLocalEvents(
                    onCreateEventClick = onCreateEventClick,
                    onJoinEventClick = onJoinEventClick,
                    onJoinLocalEventClick = onJoinLocalEventClick,
                    localEvents = state,
                    modifier = modifier
                )
            }
        }

        is SimpleScreenState.Loading -> {
            Text(text = "Loading")
        }

        is SimpleScreenState.Error -> {
            Text(text = "Error")
        }

        SimpleScreenState.Empty -> ExpensesScreenEmpty(
            onCreateEventClick = onCreateEventClick,
            onJoinEventClick = onJoinEventClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesScreenSuccess(
    state: ExpensesScreenUiModel.Expenses,
    onMenuClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onRevertExpenseClick: (expense: ExpenseUiModel) -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (debtor: DebtorShortUiModel) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "CommonEx",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = onMenuClick,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            BasicFloatingActionButton(
                text = "Операция",
                icon = Icons.Outlined.Add,
                onClick = onAddExpenseClick,
            )
        }
    ) { paddingValues ->
        val topAndHorizontalPaddings = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(topAndHorizontalPaddings)
                .padding(topAndHorizontalPaddings),
            state = rememberPullToRefreshState(),
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                EventInfoBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    eventName = state.eventName,
                    currentPersonName = state.currentPersonName
                )

                DebtsBlock(onDebtsDetailsClick, state, onReplenishmentClick)

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 4.dp),
                    text = "Операции",
                    style = MaterialTheme.typography.headlineMedium
                )

                val listState = rememberLazyListState()
                LaunchedEffect(state.expenses.size) {
                    if (state.expenses.isNotEmpty()) {
                        listState.animateScrollToItem(0)
                    }
                }

                val bottomPadding = paddingValues.calculateBottomPadding()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .consumeWindowInsets(PaddingValues(bottom = bottomPadding))
                        .padding(horizontal = 8.dp),
                    state = listState,
                    contentPadding = PaddingValues(bottom = 88.dp + bottomPadding),
                ) {
                    items(
                        count = state.expenses.size,
                        key = { index ->
                            state.expenses[state.expenses.lastIndex - index].expenseId
                        }
                    ) { index ->
                        val expense = state.expenses[state.expenses.lastIndex - index]
                        ExpenseItem(expense, onRevertExpenseClick)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesScreenLocalEvents(
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    localEvents: LocalEvents,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { BasicTopAppBar() },
        floatingActionButton = {
            BasicFloatingActionButton(
                text = "Новое событие",
                icon = Icons.Outlined.Add,
                onClick = onCreateEventClick,
            )
        },
    ) { paddingValues ->
        val topAndHorizontalPaddings = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(topAndHorizontalPaddings)
                .padding(topAndHorizontalPaddings),
        ) {
            Text(
                text = "Событие уже создано?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            JoinEventButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onJoinEventClick = onJoinEventClick
            )

            Text(
                text = "Сохранённые события",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            val bottomPadding = paddingValues.calculateBottomPadding()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(PaddingValues(bottom = bottomPadding))
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 88.dp + bottomPadding),
            ) {
                items(
                    count = localEvents.events.size,
                    key = { index -> localEvents.events[index].eventId }
                ) { index ->
                    val event = localEvents.events[index]
                    LocalEventItem(
                        event = event,
                        onJoinLocalEventClick = onJoinLocalEventClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesScreenEmpty(
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar()
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вы можете создать новое событие или присоединиться к существующему",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
            )
            NewEventButton(onCreateEventClick)
            Spacer(modifier = Modifier.height(8.dp))
            JoinEventButton(onJoinEventClick)
        }
    }
}

@Composable
private fun NewEventButton(
    onCreateEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp),
        onClick = onCreateEventClick
    ) {
        Text(text = "Новое событие")
    }
}

@Composable
private fun JoinEventButton(
    onJoinEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp),
        onClick = onJoinEventClick
    ) {
        Text(text = "Присоединиться")
    }
}

@Composable
private fun ExpenseItem(
    expense: ExpenseUiModel,
    onRevertExpenseClick: (expense: ExpenseUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable { onRevertExpenseClick.invoke(expense) }
            .fillMaxWidth()
            .border(
                border = AssistChipDefaults.assistChipBorder(false),
                shape = MaterialTheme.shapes.small,
            ),
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
                    text = expense.currencyText,
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
private fun LocalEventItem(
    event: LocalEventUiModel,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onJoinLocalEventClick(event) },
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onJoinLocalEventClick(event) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                text = event.eventName,
                style = MaterialTheme.typography.titleLarge,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun ExpensesScreenPreviewSuccessWithCreditors() {
    ExpensesTheme {
        ExpensesScreen(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            state = SimpleScreenState.Success(mockExpensesScreenUiModel(withCreditors = true))
        )
    }
}

@Preview
@Composable
private fun ExpensesScreenPreviewSuccessWithoutCreditors() {
    ExpensesTheme {
        ExpensesScreen(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            state = SimpleScreenState.Success(mockExpensesScreenUiModel(withCreditors = false))
        )
    }
}

@Composable
@Preview
private fun ExpensesScreenLocalEventsPreview() {
    ExpensesTheme {
        ExpensesScreenLocalEvents(
            onCreateEventClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            localEvents = LocalEvents(
                events = persistentListOf(
                    LocalEventUiModel(
                        eventId = 1,
                        eventName = "Local Event 1",
                    ),
                    LocalEventUiModel(
                        eventId = 2,
                        eventName = "Local Event 2",
                    ),
                )
            ),
        )
    }
}

@Composable
@Preview
private fun ExpensesScreenPreviewEmpty() {
    ExpensesTheme {
        ExpensesScreen(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            state = SimpleScreenState.Empty
        )
    }
}

internal fun mockExpensesScreenUiModel(withCreditors: Boolean): ExpensesScreenUiModel {
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
    return ExpensesScreenUiModel.Expenses(
        eventName = "France trip",
        currentPersonId = person1.id,
        currentPersonName = person1.name,
        creditors = persistentListOf(
            DebtorShortUiModel(
                personId = person1.id,
                personName = person1.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "100"
            ),
            DebtorShortUiModel(
                personId = person2.id,
                personName = person2.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "150"
            )
        ).takeIf { withCreditors } ?: persistentListOf(),
        expenses = persistentListOf(
            Expense(
                expenseId = 1,
                serverId = "11",
                currency = Currency(
                    id = 1,
                    serverId = "11",
                    code = "RUB",
                    name = "Russian Ruble",
                ),
                expenseType = ExpenseType.Spending,
                person = person1,
                subjectExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 1,
                        expenseId = 1,
                        person = person1,
                        originalAmount = 100.toBigDecimal(),
                        exchangedAmount = 100.toBigDecimal(),
                    ),
                    ExpenseSplitWithPerson(
                        expenseSplitId = 2,
                        expenseId = 1,
                        person = person2,
                        originalAmount = 150.333.toBigDecimal(),
                        exchangedAmount = 100.toBigDecimal(),
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Lunch",
            ).toUiModel("EUR"),
            Expense(
                expenseId = 2,
                serverId = "12",
                currency = Currency(
                    id = 2,
                    serverId = "11",
                    code = "USD",
                    name = "US Dollar",
                ),
                expenseType = ExpenseType.Replenishment,
                person = person2,
                subjectExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 4,
                        expenseId = 2,
                        person = person2,
                        originalAmount = 132423423.toBigDecimal(),
                        exchangedAmount = 132423423.toBigDecimal(),
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Dinner and some text",
            ).toUiModel("EUR")
        ),
        isRefreshing = false
    )
}
