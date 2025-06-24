package com.inwords.expenses.core.ui.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryOf(title: String, url: String): ClipEntry {
    return ClipEntry.withPlainText(url)
}