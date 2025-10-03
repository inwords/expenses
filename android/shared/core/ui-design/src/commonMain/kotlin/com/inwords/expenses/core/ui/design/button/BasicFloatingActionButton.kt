package com.inwords.expenses.core.ui.design.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BasicFloatingActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview
@Composable
private fun BasicFloatingActionButtonPreview() {
    ExpensesTheme {
        BasicFloatingActionButton(
            text = "Add Expense",
            icon = Icons.Outlined.Add,
            onClick = {},
        )
    }
}
