package com.inwords.expenses.feature.expenses.ui.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal

internal fun BigDecimal.toRoundedString(scale: Long = 2): String {
    val scaled = this.scale(scale)
    // lib is working very strange
    return if ((scaled * BigDecimal.TEN % 10).significand == BigDecimal.ZERO.significand) {
        scaled.toBigInteger().toString()
    } else {
        scaled.toStringExpanded()
    }
}