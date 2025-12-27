package com.inwords.expenses.feature.events.ui.local

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.TopAppBarWithText
import com.inwords.expenses.core.ui.design.button.ButtonWithIconAndText
import com.inwords.expenses.core.ui.design.button.OutlinedButtonWithText
import com.inwords.expenses.core.ui.design.legal.LegalBlock
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import com.inwords.expenses.feature.events.api.EventDeletionStateManager.EventDeletionState
import com.inwords.expenses.feature.events.ui.local.LocalEventsUiModel.LocalEventUiModel
import expenses.shared.core.ui_design.generated.resources.agree_by_continuing
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_create
import expenses.shared.feature.events.generated.resources.events_create_join_description
import expenses.shared.feature.events.generated.resources.events_delete_event
import expenses.shared.feature.events.generated.resources.events_delete_event_offline_message
import expenses.shared.feature.events.generated.resources.events_delete_local_only
import expenses.shared.feature.events.generated.resources.events_event
import expenses.shared.feature.events.generated.resources.events_event_deleted
import expenses.shared.feature.events.generated.resources.events_join
import expenses.shared.feature.events.generated.resources.events_keep_event
import expenses.shared.feature.events.generated.resources.events_your
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import expenses.shared.core.ui_design.generated.resources.Res as DesignRes


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LocalEventsPane(
    onCreateEventClick: () -> Unit,
    onJoinEventClick: () -> Unit,
    onJoinLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteEventClick: (event: LocalEventUiModel) -> Unit,
    onDeleteOnlyLocalEventClick: (event: LocalEventUiModel) -> Unit,
    onKeepLocalEventClick: (event: LocalEventUiModel) -> Unit,
    localEvents: LocalEventsUiModel,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    localEvents.recentlyRemovedEventName?.let { eventName ->
        val message = stringResource(Res.string.events_event_deleted, eventName)
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
                text = stringResource(Res.string.events_event),
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
                    text = stringResource(Res.string.events_join),
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
                ButtonWithIconAndText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = onCreateEventClick,
                    text = stringResource(Res.string.events_create),
                    imageVector = Icons.Outlined.Add,
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
            }

            Text(
                text = stringResource(Res.string.events_your),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LocalEventsEmptyPane(
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
                    text = stringResource(Res.string.events_create_join_description),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ButtonWithIconAndText(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onCreateEventClick,
                    text = stringResource(Res.string.events_create),
                    imageVector = Icons.Outlined.Add,
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButtonWithText(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onJoinEventClick,
                    text = stringResource(Res.string.events_join),
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
                text = stringResource(Res.string.events_delete_event),
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
                text = stringResource(Res.string.events_delete_event_offline_message),
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            ) {
                TextButton(onClick = { onKeepLocalEvent(event) }) {
                    Text(text = stringResource(Res.string.events_keep_event))
                }
                Button(onClick = { onRemoveLocalCopy(event) }) {
                    Text(text = stringResource(Res.string.events_delete_local_only))
                }
            }
        }
    }
}


@Composable
@Preview
private fun LocalEventsPanePreview() {
    CommonExTheme {
        LocalEventsPane(
            onCreateEventClick = {},
            onJoinEventClick = {},
            onJoinLocalEventClick = {},
            onDeleteEventClick = {},
            onDeleteOnlyLocalEventClick = {},
            onKeepLocalEventClick = {},
            localEvents = LocalEventsUiModel(
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