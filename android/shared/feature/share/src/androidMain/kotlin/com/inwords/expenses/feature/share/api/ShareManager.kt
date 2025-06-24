package com.inwords.expenses.feature.share.api

import android.content.Context
import android.content.Intent

actual class ShareManager internal constructor(
    private val context: Context
) {

    actual fun shareUrl(title: String, url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n$url")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}