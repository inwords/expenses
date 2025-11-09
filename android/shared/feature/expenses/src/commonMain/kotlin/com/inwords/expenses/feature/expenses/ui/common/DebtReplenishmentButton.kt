package com.inwords.expenses.feature.expenses.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal data class DebtShortUiModel(
    val personId: Long,
    val personName: String,
    val currencyCode: String,
    val currencyName: String,
    val amount: String,
)

@Composable
internal fun DebtReplenishmentButton(
    debt: DebtShortUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "${debt.amount} ${debt.currencyName},  ${debt.personName}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
