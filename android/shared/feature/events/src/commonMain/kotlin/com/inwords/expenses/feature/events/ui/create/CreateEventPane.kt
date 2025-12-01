package com.inwords.expenses.feature.events.ui.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.TopAppBarWithNavIconAndText
import com.inwords.expenses.core.ui.design.button.ButtonWithIconAndText
import com.inwords.expenses.core.ui.design.group.MultiSelectConnectedButtonGroupWithFlowLayout
import com.inwords.expenses.core.ui.design.group.ToggleButtonOption
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import com.inwords.expenses.feature.events.ui.common.EventNameField
import com.inwords.expenses.feature.events.ui.create.CreateEventPaneUiModel.CurrencyInfoUiModel
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.common_back
import expenses.shared.feature.events.generated.resources.events_create_title
import expenses.shared.feature.events.generated.resources.events_currency_section_title
import expenses.shared.feature.events.generated.resources.events_participants_title
import expenses.shared.feature.events.generated.resources.events_section_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun CreateEventPane(
    modifier: Modifier = Modifier,
    state: CreateEventPaneUiModel,
    onEventNameChanged: (String) -> Unit,
    onCurrencyClicked: (CurrencyInfoUiModel) -> Unit,
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
                title = stringResource(Res.string.events_create_title),
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
                text = stringResource(Res.string.events_section_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            EventNameField(
                modifier = Modifier
                    .fillMaxWidth(),
                eventName = state.eventName,
                onDone = onConfirmClicked,
                onEventNameChanged = onEventNameChanged
            )

            // TODO duplicate UI
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
                text = stringResource(Res.string.events_currency_section_title),
                style = MaterialTheme.typography.headlineMedium
            )
            MultiSelectConnectedButtonGroupWithFlowLayout(
                options = state.currencies.map { currencyInfo ->
                    ToggleButtonOption(
                        text = currencyInfo.currencyName,
                        checked = currencyInfo.selected,
                        payload = currencyInfo
                    )
                },
                onCheckedChange = { _, _, currencyInfo -> onCurrencyClicked.invoke(currencyInfo) },
            )

            Spacer(modifier = Modifier.weight(1f))

            ButtonWithIconAndText(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 16.dp),
                onClick = onConfirmClicked,
                enabled = state.eventName.isNotBlank(),
                text = stringResource(Res.string.events_participants_title),
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                minHeight = ButtonDefaults.MediumContainerHeight,
            )
        }
    }
}

@Preview
@Composable
private fun CreateEventPanePreview() {
    CommonExTheme {
        CreateEventPane(
            state = CreateEventPaneUiModel(
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
            onNavIconClicked = {},
        )
    }
}
