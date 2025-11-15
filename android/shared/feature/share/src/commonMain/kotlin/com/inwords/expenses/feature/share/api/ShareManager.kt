package com.inwords.expenses.feature.share.api

expect class ShareManager {

    suspend fun shareUrl(title: String, url: String)
}