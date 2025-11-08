package com.inwords.expenses.feature.events.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.BasicTopAppBar
import com.inwords.expenses.core.ui.design.button.BasicButton
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.feature.events.ui.common.EventNameField
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenUiModel.CurrencyInfoUiModel
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CreateEventScreen(
    modifier: Modifier = Modifier,
    state: CreateEventScreenUiModel,
    onEventNameChanged: (String) -> Unit,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
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
                enabled = state.eventName.isNotBlank(),
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
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                EventNameField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    eventName = state.eventName,
                    onDone = onConfirmClicked,
                    onEventNameChanged = onEventNameChanged
                )

                // TODO duplicate UI
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    text = "Валюта",
                    style = MaterialTheme.typography.headlineMedium
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.currencies.forEach { currencyInfo ->
                        InputChip(
                            selected = currencyInfo.selected,
                            onClick = { onCurrencyClicked.invoke(currencyInfo) },
                            label = { Text(text = currencyInfo.currencyName) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateEventScreenPreview() {
    ExpensesTheme {
        CreateEventScreen(
            state = CreateEventScreenUiModel(
                eventName = "",
                currencies = persistentListOf(
                    CurrencyInfoUiModel(
                        currencyName = "US Dollar",
                        currencyCode = "USD",
                        selected = false
                    ),
                    CurrencyInfoUiModel(
                        currencyName = "Euro",
                        currencyCode = "EUR",
                        selected = true
                    ),
                    CurrencyInfoUiModel(
                        currencyName = "Russian Ruble",
                        currencyCode = "RUB",
                        selected = false
                    ),
                    CurrencyInfoUiModel(
                        currencyName = "Turkish Lira",
                        currencyCode = "TRY",
                        selected = false
                    ),
                    CurrencyInfoUiModel(
                        currencyName = "Japanese Yen",
                        currencyCode = "JPY",
                        selected = false
                    )
                )
            ),
            onEventNameChanged = {},
            onCurrencyClicked = {},
            onConfirmClicked = {},
        )
    }
}
