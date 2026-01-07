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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.TopAppBarWithNavIconAndText
import com.inwords.expenses.core.ui.design.loading.DefaultProgressIndicator
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.expenses.ui.common.DebtReplenishmentButton
import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel.PersonUiModel
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.common_back
import expenses.shared.feature.expenses.generated.resources.common_error
import expenses.shared.feature.expenses.generated.resources.expenses_no_expenses
import expenses.shared.feature.expenses.generated.resources.expenses_owes
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DebtsListPane(
    modifier: Modifier = Modifier,
    onReplenishmentClick: (debtor: PersonUiModel, creditor: DebtShortUiModel) -> Unit,
    onNavIconClicked: () -> Unit,
    state: SimpleScreenState<DebtsListPaneUiModel>,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBarWithNavIconAndText(
                onNavIconClicked = onNavIconClicked,
                title = (state as? SimpleScreenState.Success)?.data?.eventName ?: "...",
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.common_back),
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
                Text(text = stringResource(Res.string.common_error))
            }

            SimpleScreenState.Empty -> {
                Text(text = stringResource(Res.string.expenses_no_expenses))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun DebtsListPaneSuccess(
    state: DebtsListPaneUiModel,
    paddingValues: PaddingValues,
    onReplenishmentClick: (debtor: PersonUiModel, creditor: DebtShortUiModel) -> Unit,
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
                    text = "${debtor.personName} ${stringResource(Res.string.expenses_owes)}",
                    style = MaterialTheme.typography.headlineMedium
                )
                state.creditors[debtor]!!.forEach { debtorDebt ->
                    FlowRow(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DebtReplenishmentButton(
                            modifier = Modifier.testTag("debts_list_debt_button"),
                            debt = debtorDebt,
                            onClick = { onReplenishmentClick.invoke(debtor, debtorDebt) },
                        )
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

@Preview
@Composable
private fun DebtsListPanePreview() {
    DebtsListPane(
        state = SimpleScreenState.Success(mockDebtsListPaneUiModel()),
        onReplenishmentClick = { _, _ -> },
        onNavIconClicked = { },
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
                DebtShortUiModel(
                    personId = person2.personId,
                    personName = person2.personName,
                    currencyCode = "EUR",
                    currencyName = "Euro",
                    amount = "100"
                )
            ),
            person2 to persistentListOf(
                DebtShortUiModel(
                    personId = person1.personId,
                    personName = person1.personName,
                    currencyCode = "RUB",
                    currencyName = "Russian Ruble",
                    amount = "1000"
                )
            )
        )
    )
}
