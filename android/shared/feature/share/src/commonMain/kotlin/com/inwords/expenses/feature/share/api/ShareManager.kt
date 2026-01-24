package com.inwords.expenses.feature.share.api

expect class ShareManager {

    suspend fun shareText(title: String, url: String)
}