package com.inwords.expenses.core.ui.design.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
    enabled: Boolean = true,
) {
    val containerColor = if (enabled) {
        FloatingActionButtonDefaults.containerColor
    } else {
        FloatingActionButtonDefaults.containerColor.copy(alpha = 0.6f)
    }
    val contentColor = if (enabled) {
        contentColorFor(containerColor)
    } else {
        contentColorFor(containerColor).copy(alpha = 0.6f)
    }
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = if (enabled) {
            onClick
        } else {
            {}
        },
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(icon, contentDescription = null)
    }
}

@Preview
@Composable
private fun BasicFloatingActionButtonDisabledPreview() {
    ExpensesTheme {
        BasicFloatingActionButton(
            text = "Add Expense",
            icon = Icons.Outlined.Add,
            onClick = {},
            enabled = false,
        )
    }
}


@Preview
@Composable
private fun BasicFloatingActionButtonEnabledPreview() {
    ExpensesTheme {
        BasicFloatingActionButton(
            text = "Add Expense",
            icon = Icons.Outlined.Add,
            onClick = {},
            enabled = true,
        )
    }
}
