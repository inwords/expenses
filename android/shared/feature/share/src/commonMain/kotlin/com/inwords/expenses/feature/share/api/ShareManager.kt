package com.inwords.expenses.feature.share.api

expect class ShareManager {

    fun shareUrl(title: String, url: String)
}