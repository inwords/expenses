package com.inwords.expenses.feature.events.ui.join

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.BasicTopAppBar
import com.inwords.expenses.core.ui.design.button.BasicButton
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.feature.events.ui.common.EventAccessCodeField
import com.inwords.expenses.feature.events.ui.common.EventIdField
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun JoinEventScreen(
    modifier: Modifier = Modifier,
    state: JoinEventScreenUiModel,
    onEventIdChanged: (String) -> Unit,
    onEventAccessCodeChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            BasicTopAppBar()
        },
        floatingActionButton = {
            BasicButton(
                text = "Участники",
                icon = Icons.AutoMirrored.Outlined.ArrowForward,
                onClick = onConfirmClicked,
                enabled = state.eventId.isNotBlank() && state.eventAccessCode.isNotBlank(),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.Center),
            ) {
                Text(
                    text = "Введите ID события и код доступа, чтобы присоединиться",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth(0.7f),
                )

                EventIdField(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(0.7f),
                    eventId = state.eventId,
                    onEventIdChanged = onEventIdChanged
                )

                EventAccessCodeField(
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    eventAccessCode = state.eventAccessCode,
                    onDone = onConfirmClicked,
                    onEventAccessCodeChanged = onEventAccessCodeChanged,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventsScreenPreview() {
    ExpensesTheme {
        JoinEventScreen(
            state = JoinEventScreenUiModel(
                eventId = "",
                eventAccessCode = ""
            ),
            onEventIdChanged = {},
            onEventAccessCodeChanged = {},
            onConfirmClicked = {},
        )
    }
}
