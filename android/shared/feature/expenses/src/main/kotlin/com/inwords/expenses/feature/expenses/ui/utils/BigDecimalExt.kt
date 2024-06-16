package com.inwords.expenses.feature.expenses.ui.utils

import java.math.BigDecimal
import java.math.RoundingMode

internal fun BigDecimal.toRoundedString(scale: Int = 1): String {
    val scaled = this.setScale(scale, RoundingMode.HALF_EVEN)
    return if ((scaled % BigDecimal.ONE).unscaledValue() == BigDecimal.ZERO.unscaledValue()) {
        scaled.toBigInteger().toString()
    } else {
        scaled.toPlainString()
    }
}