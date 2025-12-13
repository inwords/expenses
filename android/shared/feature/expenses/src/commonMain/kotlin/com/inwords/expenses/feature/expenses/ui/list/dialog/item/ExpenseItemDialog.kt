package com.inwords.expenses.feature.expenses.ui.list.dialog.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_revert_operation
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExpenseItemDialog(
    state: ExpenseItemDialogUiModel,
    onRevertExpenseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = state.description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onRevertExpenseClick),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(Res.string.expenses_revert_operation),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun ExpenseItemDialogPreview() {
    CommonExTheme {
        ExpenseItemDialog(
            state = ExpenseItemDialogUiModel(
                description = "Булка с маслом из хорошей булочной, что тут ещё сказать",
            ),
            onRevertExpenseClick = {},
        )
    }
}
