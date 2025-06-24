package com.inwords.expenses.core.ui.utils

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(title: String, url: String): ClipEntry {
    return ClipEntry(
        ClipData(
            title,
            arrayOf("text/plain"),
            ClipData.Item(url)
        )
    )
}