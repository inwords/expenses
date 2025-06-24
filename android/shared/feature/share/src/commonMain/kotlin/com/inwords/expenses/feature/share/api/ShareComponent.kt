package com.inwords.expenses.feature.share.api

import com.inwords.expenses.core.utils.Component

class ShareComponent internal constructor(
    val shareManagerLazy: Lazy<ShareManager>,
) : Component