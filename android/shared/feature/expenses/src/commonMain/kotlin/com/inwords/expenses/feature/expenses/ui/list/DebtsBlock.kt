package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inwords.expenses.feature.expenses.ui.common.DebtReplenishmentButton
import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun DebtsBlock(
    onDebtsDetailsClick: () -> Unit,
    state: ExpensesPaneUiModel.Expenses,
    onReplenishmentClick: (DebtShortUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Долги",
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedButton(
                onClick = onDebtsDetailsClick
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = "детализация",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (state.debts.isEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Отсутствуют",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                )
            }

        } else {
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.debts.forEach { debt ->
                    DebtReplenishmentButton(debt = debt, onClick = { onReplenishmentClick.invoke(debt) })
                }
            }
        }
    }
}
