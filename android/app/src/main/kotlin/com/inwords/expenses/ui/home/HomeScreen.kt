package com.inwords.expenses.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.ui.home.model.HomeScreenUiModel
import com.inwords.expenses.ui.theme.ExpensesTheme

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToExpenses: () -> Unit,
    state: HomeScreenUiModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = onNavigateToExpenses) {
            Text(text = "Expenses")
        }
    }
}

@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun HomeScreenPreview() {
    ExpensesTheme {
        Surface {
            HomeScreen(
                onNavigateToExpenses = {},
                state = HomeScreenUiModel(
                    "Дайте ещё этих сладких Французских булок",
                ),
            )
        }
    }
}