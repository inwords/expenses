package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenUiModel.PersonUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Preview(showBackground = true)
@Composable
private fun DebtsListScreenPreview() {
    DebtsListScreen(
        state = SimpleScreenState.Success(mockDebtsListScreenUiModel()),
        onReplenishmentClick = { _, _ -> },
        onCloseClick = { }
    )
}

internal fun mockDebtsListScreenUiModel(): DebtsListScreenUiModel {
    val person1 = PersonUiModel(
        personId = 1,
        personName = "Василий"
    )
    val person2 = PersonUiModel(
        personId = 2,
        personName = "Максим"
    )
    return DebtsListScreenUiModel(
        eventName = "Event",
        creditors = persistentMapOf(
            person1 to persistentListOf(
                DebtorShortUiModel(
                    person = person2,
                    currencyCode = "EUR",
                    currencyName = "Euro",
                    amount = "100"
                )
            ),
            person2 to persistentListOf(
                DebtorShortUiModel(
                    person = person1,
                    currencyCode = "RUB",
                    currencyName = "Russian Ruble",
                    amount = "1000"
                )
            )
        )
    )
}
