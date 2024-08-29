package com.inwords.expenses.feature.events.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inwords.expenses.feature.events.ui.common.EventNameField
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenUiModel.CurrencyInfoUiModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CreateEventScreen(
    modifier: Modifier = Modifier,
    state: CreateEventScreenUiModel,
    onEventNameChanged: (String) -> Unit,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = modifier.fillMaxWidth().align(Alignment.Center)) {
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

        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onConfirmClicked
        ) {
            Text(text = "Участники")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        }
    }
}
