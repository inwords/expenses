package com.inwords.expenses.feature.events.ui.join

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.TopAppBarWithNavIconAndText
import com.inwords.expenses.core.ui.design.button.ButtonWithIconAndText
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import com.inwords.expenses.feature.events.ui.common.EventAccessCodeField
import com.inwords.expenses.feature.events.ui.common.EventIdField
import com.inwords.expenses.feature.events.ui.join.JoinEventPaneUiModel.EventJoiningState
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.common_back
import expenses.shared.feature.events.generated.resources.events_join_description
import expenses.shared.feature.events.generated.resources.events_participants_title
import expenses.shared.feature.events.generated.resources.events_section_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun JoinEventPane(
    modifier: Modifier = Modifier,
    state: JoinEventPaneUiModel,
    onEventIdChanged: (String) -> Unit,
    onEventAccessCodeChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    onNavIconClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBarWithNavIconAndText(
                onNavIconClicked = onNavIconClicked,
                title = stringResource(Res.string.events_section_title),
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.common_back),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .consumeWindowInsets(paddingValues)
                .padding(horizontal = 8.dp)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(Res.string.events_join_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 32.dp),
            )

            val joining = state.joining == EventJoiningState.Joining
            val joiningError = state.joining as? EventJoiningState.Error
            EventIdField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                eventId = state.eventId,
                onEventIdChanged = onEventIdChanged,
                enabled = !joining,
                isError = joiningError != null,
            )

            Row(
                modifier = Modifier
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .weight(1f),
                    visible = joiningError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = joiningError?.message.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                EventAccessCodeField(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    eventAccessCode = state.eventAccessCode,
                    onDone = onConfirmClicked,
                    onEventAccessCodeChanged = onEventAccessCodeChanged,
                    enabled = !joining,
                    isError = joiningError != null,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = joining,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LoadingIndicator()
                }

                ButtonWithIconAndText(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    onClick = onConfirmClicked,
                    enabled = !joining && state.eventId.isNotBlank() && state.eventAccessCode.isNotBlank(),
                    text = stringResource(Res.string.events_participants_title),
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    minHeight = ButtonDefaults.MediumContainerHeight,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventsPanePreview() {
    CommonExTheme {
        JoinEventPane(
            state = JoinEventPaneUiModel(
                eventId = "",
                eventAccessCode = "",
                joining = EventJoiningState.None
            ),
            onEventIdChanged = {},
            onEventAccessCodeChanged = {},
            onConfirmClicked = {},
            onNavIconClicked = {},
        )
    }
}
