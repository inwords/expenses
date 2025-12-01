package com.inwords.expenses.core.ui.utils

import androidx.compose.ui.platform.UriHandler

fun UriHandler.openUriSafe(uri: String): Boolean {
    return try {
        this.openUri(uri)
        true
    } catch (_: IllegalArgumentException) {
        // TODO: log error
        false
    }
}
