package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.TopAppBarWithText
import com.inwords.expenses.core.ui.design.button.BasicFloatingActionButton
import com.inwords.expenses.core.ui.design.button.ButtonWithIconAndText
import com.inwords.expenses.core.ui.design.button.OutlinedButtonWithText
import com.inwords.expenses.core.ui.design.legal.LegalBlock
import com.inwords.expenses.core.ui.design.loading.DefaultProgressIndicator
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.EventsInteractor.EventDeletionState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.ui.common.EventInfoBlock
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel.Expenses.ExpenseUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel.LocalEvents
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel.LocalEvents.LocalEventUiModel
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import expenses.shared.core.ui_design.generated.resources.agree_by_continuing
import expenses.shared.feature.events.generated.resources.events_delete_event
import expenses.shared.feature.events.generated.resources.events_keep_event
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.common_error
import expenses.shared.feature.expenses.generated.resources.expenses_app_name
import expenses.shared.feature.expenses.generated.resources.expenses_create
import expenses.shared.feature.expenses.generated.resources.expenses_create_join_description
import expenses.shared.feature.expenses.generated.resources.expenses_delete_event_offline_message
import expenses.shared.feature.expenses.generated.resources.expenses_delete_local_only
import expenses.shared.feature.expenses.generated.resources.expenses_event
import expenses.shared.feature.expenses.generated.resources.expenses_event_deleted
import expenses.shared.feature.expenses.generated.resources.expenses_join
import expenses.shared.feature.expenses.generated.resources.expenses_operation
import expenses.shared.feature.expenses.generated.resources.expenses_operations
import expenses.shared.feature.expenses.generated.resources.expenses_your
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import expenses.shared.core.ui_design.generated.resources.Res as DesignRes
import expenses.shared.feature.events.generated.resources.Res as EventsRes


@Composable
internal fun ExpensesPane(
    state: SimpleScreenState<ExpensesPaneUiModel>,
    onMenuClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onRevertExpenseClick: (expense: ExpenseUiModel) -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (debtor: DebtShortUiModel) -> Unit,
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteOnlyLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onKeepLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SimpleScreenState.Success -> {
            when (val state = state.data) {
                is ExpensesPaneUiModel.Expenses -> ExpensesPaneSuccess(
                    state = state,
                    onMenuClick = onMenuClick,
                    onAddExpenseClick = onAddExpenseClick,
                    onRevertExpenseClick = onRevertExpenseClick,
                    onDebtsDetailsClick = onDebtsDetailsClick,
                    onReplenishmentClick = onReplenishmentClick,
                    onRefresh = onRefresh,
                    modifier = modifier
                )

                is LocalEvents -> ExpensesPaneLocalEvents(
                    onCreateEventClick = onCreateEventClick,
                    onJoinEventClick = onJoinEventClick,
                    onJoinLocalEventClick = onJoinLocalEventClick,
                    onDeleteEventClick = onDeleteEventClick,
                    onDeleteOnlyLocalEventClick = onDeleteOnlyLocalEventClick,
                    onKeepLocalEventClick = onKeepLocalEventClick,
                    localEvents = state,
                    modifier = modifier
                )
            }
        }

        is SimpleScreenState.Loading -> ExpensesPaneLoading(modifier)

        is SimpleScreenState.Error -> {
            Text(text = stringResource(Res.string.common_error))
        }

        SimpleScreenState.Empty -> ExpensesPaneEmpty(
            onCreateEventClick = onCreateEventClick,
            onJoinEventClick = onJoinEventClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ExpensesPaneSuccess(
    state: ExpensesPaneUiModel.Expenses,
    onMenuClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onRevertExpenseClick: (expense: ExpenseUiModel) -> Unit,
    onDebtsDetailsClick: () -> Unit,
    onReplenishmentClick: (debtor: DebtShortUiModel) -> Unit,
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
                            text = stringResource(Res.string.expenses_app_name),
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .testTag("expenses_menu_button"),
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
                text = stringResource(Res.string.expenses_operation),
                imageVector = Icons.Outlined.Add,
                onClick = onAddExpenseClick,
            )
        }
    ) { paddingValues ->
        val topAndHorizontalPaddings = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
        )

        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(topAndHorizontalPaddings)
                .padding(topAndHorizontalPaddings),
            state = pullToRefreshState,
            isRefreshing = state.isRefreshing,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isRefreshing,
                    state = pullToRefreshState,
                )
            },
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
                    text = stringResource(Res.string.expenses_operations),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ExpensesPaneLocalEvents(
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteOnlyLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onKeepLocalEventClick: (event: LocalEventUiModel) -> Unit,
    localEvents: LocalEvents,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    localEvents.recentlyRemovedEventName?.let { eventName ->
        val message = stringResource(Res.string.expenses_event_deleted, eventName)
        LaunchedEffect(eventName) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBarWithText() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                text = stringResource(Res.string.expenses_event),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButtonWithText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = onJoinEventClick,
                    text = stringResource(Res.string.expenses_join),
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
                ButtonWithIconAndText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = onCreateEventClick,
                    text = stringResource(Res.string.expenses_create),
                    imageVector = Icons.Outlined.Add,
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
            }

            Text(
                text = stringResource(Res.string.expenses_your),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            val bottomPadding = paddingValues.calculateBottomPadding()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(PaddingValues(bottom = bottomPadding))
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp + bottomPadding),
            ) {
                items(
                    count = localEvents.events.size,
                    key = { index -> localEvents.events[index].eventId }
                ) { index ->
                    val event = localEvents.events[index]
                    LocalEventItem(
                        event = event,
                        onJoinLocalEventClick = onJoinLocalEventClick,
                        onDeleteEventClick = onDeleteEventClick,
                        onDeleteOnlyLocalEventClick = onDeleteOnlyLocalEventClick,
                        onKeepLocalEventClick = onKeepLocalEventClick,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesPaneLoading(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBarWithText() },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            DefaultProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ExpensesPaneEmpty(
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBarWithText() },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.expenses_create_join_description),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ButtonWithIconAndText(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onCreateEventClick,
                    text = stringResource(Res.string.expenses_create),
                    imageVector = Icons.Outlined.Add,
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButtonWithText(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onJoinEventClick,
                    text = stringResource(Res.string.expenses_join),
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(DesignRes.string.agree_by_continuing),
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.height(4.dp))
                LegalBlock(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onPrivacyPolicyClicked = { /* No additional action needed - URL opening handled by component */ },
                    onTermsOfUseClicked = { /* No additional action needed - URL opening handled by component */ }
                )
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LocalEventItem(
    event: LocalEventUiModel,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteOnlyLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onKeepLocalEventClick: (event: LocalEventUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (event.deletionState) {
        EventDeletionState.None, EventDeletionState.Loading -> {
            val state = rememberSwipeToDismissBoxState()
            val scope = rememberCoroutineScope()
            SwipeToDismissBox(
                modifier = modifier.fillMaxWidth(),
                state = state,
                enableDismissFromStartToEnd = false,
                onDismiss = {
                    onDeleteEventClick(event)
                    scope.launch { state.reset() }
                },
                backgroundContent = {
                    LocalEventDismissBackground(
                        modifier = modifier.fillMaxSize(),
                    )
                },
                content = {
                    LocalEventCard(
                        modifier = modifier.fillMaxWidth(),
                        event = event,
                        deletionInProgress = event.deletionState == EventDeletionState.Loading,
                        onJoinLocalEventClick = onJoinLocalEventClick,
                    )
                }
            )
        }

        EventDeletionState.RemoteDeletionFailed -> LocalEventDeletionResolution(
            modifier = modifier.fillMaxWidth(),
            event = event,
            onRemoveLocalCopy = onDeleteOnlyLocalEventClick,
            onKeepLocalEvent = onKeepLocalEventClick,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LocalEventDismissBackground(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.error)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(EventsRes.string.events_delete_event),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onError,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.size(ButtonDefaults.LargeIconSize),
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LocalEventCard(
    event: LocalEventUiModel,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    deletionInProgress: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = { onJoinLocalEventClick(event) },
        enabled = !deletionInProgress,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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


            if (deletionInProgress) {
                LoadingIndicator(
                    modifier = Modifier.size(ButtonDefaults.LargeIconSize),
                )
            } else {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.LargeIconSize),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun LocalEventDeletionResolution(
    event: LocalEventUiModel,
    onRemoveLocalCopy: (event: LocalEventUiModel) -> Unit,
    onKeepLocalEvent: (event: LocalEventUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = event.eventName,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(Res.string.expenses_delete_event_offline_message),
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            ) {
                TextButton(onClick = { onKeepLocalEvent(event) }) {
                    Text(text = stringResource(EventsRes.string.events_keep_event))
                }
                Button(onClick = { onRemoveLocalCopy(event) }) {
                    Text(text = stringResource(Res.string.expenses_delete_local_only))
                }
            }
        }
    }
}


@Preview
@Composable
private fun ExpensesPanePreviewSuccessWithCreditors() {
    CommonExTheme {
        ExpensesPane(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            state = SimpleScreenState.Success(mockExpensesPaneUiModel(withDebts = true))
        )
    }
}

@Preview
@Composable
private fun ExpensesPanePreviewSuccessWithoutCreditors() {
    CommonExTheme {
        ExpensesPane(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            state = SimpleScreenState.Success(mockExpensesPaneUiModel(withDebts = false))
        )
    }
}

@Composable
@Preview
private fun ExpensesPaneLocalEventsPreview() {
    CommonExTheme {
        ExpensesPaneLocalEvents(
            onCreateEventClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            localEvents = LocalEvents(
                events = persistentListOf(
                    LocalEventUiModel(
                        eventId = 1,
                        eventName = "Local Event 1",
                        deletionState = EventDeletionState.None,
                    ),
                    LocalEventUiModel(
                        eventId = 2,
                        eventName = "Local Event 2",
                        deletionState = EventDeletionState.RemoteDeletionFailed,
                    ),
                ),
                recentlyRemovedEventName = null,
            ),
        )
    }
}

@Composable
@Preview
private fun ExpensesPanePreviewEmpty() {
    CommonExTheme {
        ExpensesPane(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            state = SimpleScreenState.Empty
        )
    }
}

@Composable
@Preview
private fun ExpensesPanePreviewLoading() {
    CommonExTheme {
        ExpensesPane(
            onMenuClick = {},
            onAddExpenseClick = {},
            onRevertExpenseClick = {},
            onDebtsDetailsClick = {},
            onReplenishmentClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onCreateEventClick = {},
            onRefresh = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            state = SimpleScreenState.Loading
        )
    }
}

internal fun mockExpensesPaneUiModel(withDebts: Boolean): ExpensesPaneUiModel {
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
    return ExpensesPaneUiModel.Expenses(
        eventName = "France trip",
        currentPersonId = person1.id,
        currentPersonName = person1.name,
        debts = persistentListOf(
            DebtShortUiModel(
                personId = person1.id,
                personName = person1.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "100"
            ),
            DebtShortUiModel(
                personId = person2.id,
                personName = person2.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "150"
            )
        ).takeIf { withDebts } ?: persistentListOf(),
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
