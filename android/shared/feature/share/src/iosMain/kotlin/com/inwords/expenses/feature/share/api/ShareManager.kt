package com.inwords.expenses.feature.share.api

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareManager internal constructor() {

    @OptIn(BetaInteropApi::class)
    actual fun shareUrl(title: String, url: String) {
        val activityItems = listOf(
            NSString.Companion.create(string = "$title\n$url")
        )
        val activityViewController = UIActivityViewController(activityItems = activityItems, applicationActivities = null)

        // Get the top-most view controller to present the activity view controller
        val rootViewController = UIApplication.Companion.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
    }

}