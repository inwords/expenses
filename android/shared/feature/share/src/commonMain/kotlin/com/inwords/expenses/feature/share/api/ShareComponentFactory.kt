package com.inwords.expenses.feature.share.api

expect class ShareComponentFactory {

    interface Deps

    fun create(): ShareComponent
}
