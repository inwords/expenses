package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.loading.DefaultProgressIndicator
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel.PersonUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DebtsListPane(
    modifier: Modifier = Modifier,
    onReplenishmentClick: (debtor: PersonUiModel, creditor: DebtorShortUiModel) -> Unit,
    onCloseClick: () -> Unit,
    state: SimpleScreenState<DebtsListPaneUiModel>,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = (state as? SimpleScreenState.Success)?.data?.eventName ?: "...")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onCloseClick
                    ) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        when (state) {
            is SimpleScreenState.Success -> DebtsListPaneSuccess(
                state = state.data,
                paddingValues = paddingValues,
                onReplenishmentClick = onReplenishmentClick,
                modifier = modifier,
            )

            is SimpleScreenState.Loading -> DebtsListPaneLoading(
                paddingValues = paddingValues,
                modifier = modifier,
            )

            is SimpleScreenState.Error -> {
                Text(text = "Error")
            }

            SimpleScreenState.Empty -> {
                Text(text = "No expenses")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun DebtsListPaneSuccess(
    state: DebtsListPaneUiModel,
    paddingValues: PaddingValues,
    onReplenishmentClick: (debtor: PersonUiModel, creditor: DebtorShortUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val topAndHorizontalPaddings = PaddingValues(
        top = paddingValues.calculateTopPadding(),
        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .consumeWindowInsets(topAndHorizontalPaddings)
            .padding(topAndHorizontalPaddings)
            .verticalScroll(rememberScrollState())
    ) {
        state.creditors.keys.forEachIndexed { index, debtor ->
            Column {
                if (index != 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "${debtor.personName} должен",
                    style = MaterialTheme.typography.headlineMedium
                )
                state.creditors[debtor]!!.forEach { debtorDebt ->
                    FlowRow(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ReturnCreditorDebtButton(debtorDebt, { onReplenishmentClick.invoke(debtor, debtorDebt) })
                    }
                }
                if (index == state.creditors.keys.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp + paddingValues.calculateBottomPadding()))
                }
            }
        }
    }
}

@Composable
private fun DebtsListPaneLoading(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
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

@Composable
private fun ReturnCreditorDebtButton(
    creditor: DebtorShortUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = "${creditor.amount} ${creditor.currencyName},  ${creditor.person.personName}",
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
    }
}

@Preview
@Composable
private fun DebtsListPanePreview() {
    DebtsListPane(
        state = SimpleScreenState.Success(mockDebtsListPaneUiModel()),
        onReplenishmentClick = { _, _ -> },
        onCloseClick = { }
    )
}

internal fun mockDebtsListPaneUiModel(): DebtsListPaneUiModel {
    val person1 = PersonUiModel(
        personId = 1,
        personName = "Василий"
    )
    val person2 = PersonUiModel(
        personId = 2,
        personName = "Максим"
    )
    return DebtsListPaneUiModel(
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
