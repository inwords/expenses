package com.inwords.expenses.core.ui.design.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BasicFloatingActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = if (enabled) {
            onClick
        } else {
            {}
        },
        containerColor = if (enabled) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        },
        contentColor = if (enabled) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
        }
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(icon, contentDescription = null)
    }
}