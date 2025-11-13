package com.inwords.expenses.feature.events.ui.join

import androidx.compose.foundation.layout.Column
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
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.feature.events.ui.common.EventAccessCodeField
import com.inwords.expenses.feature.events.ui.common.EventIdField

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
                title = "Событие",
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Назад",
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
                text = "Введите ID события и код доступа, чтобы присоединиться",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 32.dp),
            )

            EventIdField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                eventId = state.eventId,
                onEventIdChanged = onEventIdChanged
            )

            EventAccessCodeField(
                modifier = Modifier
                    .align(Alignment.End)
                    .fillMaxWidth(0.5f),
                eventAccessCode = state.eventAccessCode,
                onDone = onConfirmClicked,
                onEventAccessCodeChanged = onEventAccessCodeChanged,
            )

            Spacer(modifier = Modifier.weight(1f))

            ButtonWithIconAndText(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 16.dp),
                onClick = onConfirmClicked,
                enabled = state.eventId.isNotBlank() && state.eventAccessCode.isNotBlank(),
                text = "Участники",
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                minHeight = ButtonDefaults.MediumContainerHeight,
            )
        }
    }
}

@Preview
@Composable
private fun EventsPanePreview() {
    ExpensesTheme {
        JoinEventPane(
            state = JoinEventPaneUiModel(
                eventId = "",
                eventAccessCode = ""
            ),
            onEventIdChanged = {},
            onEventAccessCodeChanged = {},
            onConfirmClicked = {},
            onNavIconClicked = {},
        )
    }
}
