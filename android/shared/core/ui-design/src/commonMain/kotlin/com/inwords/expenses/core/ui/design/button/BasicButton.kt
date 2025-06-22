package com.inwords.expenses.core.ui.design.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BasicButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(icon, contentDescription = null)
    }
}

@Preview
@Composable
private fun BasicButtonDisabledPreview() {
    ExpensesTheme {
        Surface {
            BasicButton(
                text = "Add Expense",
                icon = Icons.Outlined.Add,
                onClick = {},
                enabled = false,
            )
        }
    }
}


@Preview
@Composable
private fun BasicButtonEnabledPreview() {
    ExpensesTheme {
        Surface {
            BasicButton(
                text = "Add Expense",
                icon = Icons.Outlined.Add,
                onClick = {},
                enabled = true,
            )
        }
    }
}
