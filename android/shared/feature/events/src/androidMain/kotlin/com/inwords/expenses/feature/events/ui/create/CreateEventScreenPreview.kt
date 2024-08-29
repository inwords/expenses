package com.inwords.expenses.feature.events.ui.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenUiModel.CurrencyInfoUiModel
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun CreateEventScreenPreview() {
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
                    selected = false
                ),
                CurrencyInfoUiModel(
                    currencyName = "Russian Ruble",
                    currencyCode = "RUB",
                    selected = false
                )
            )
        ),
        onEventNameChanged = {},
        onCurrencyClicked = {},
        onConfirmClicked = {},
    )
}