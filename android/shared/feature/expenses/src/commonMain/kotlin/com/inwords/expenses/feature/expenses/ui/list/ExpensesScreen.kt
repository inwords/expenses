package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.ExpenseUiModel

@Composable
internal fun ExpensesScreen(
    state: SimpleScreenState<ExpensesScreenUiModel>,
    onAddExpenseClick: () -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (ExpensesScreenUiModel.DebtorShortUiModel) -> Unit,
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> ExpensesScreenSuccess(
            state = state.data,
            onAddExpenseClick = onAddExpenseClick,
            onDebtsDetailsClick = onDebtsDetailsClick,
            onReplenishmentClick = onReplenishmentClick,
            onRefresh = onRefresh,
            modifier = modifier,
        )

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ExpensesScreenSuccess(
    state: ExpensesScreenUiModel,
    onAddExpenseClick: () -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (ExpensesScreenUiModel.DebtorShortUiModel) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val appAndPersonName = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                                append("CommonEx")
                            }
                            append("  ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                                append(state.currentPersonName)
                            }
                        }
                        Text(text = appAndPersonName)

                        Spacer(modifier = Modifier.weight(1f))

                        val idAndPin = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontWeight = FontWeight.Light
                                )
                            ) {
                                append("  ID ")
                                append(state.eventId)
                                append(", PIN ")
                                append(state.pinCode)
                            }
                        }
                        Text(idAndPin)
                    }
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

                val bottomPadding = paddingValues.calculateBottomPadding()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .consumeWindowInsets(PaddingValues(bottom = bottomPadding))
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp + bottomPadding),
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
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                            append("CommonEx")
                        }
                    }
                    Text(text = text)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 16.dp),
                onClick = onCreateEventClick
            ) {
                Text(text = "Create event")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 16.dp),
                onClick = onJoinEventClick
            ) {
                Text(text = "Join event")
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
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
