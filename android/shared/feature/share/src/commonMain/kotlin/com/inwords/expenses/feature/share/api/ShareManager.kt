package com.inwords.expenses.feature.share.api

expect class ShareManager {

    /**
     * Share text content.
     * @param subject Subject line for apps that support it (e.g., email). Can be empty.
     * @param fullText The full text content to share.
     */
    suspend fun shareText(subject: String, fullText: String)
}
