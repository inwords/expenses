package com.inwords.expenses.kmmsharedmodule

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform