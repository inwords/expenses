package com.inwords.expenses.feature.expenses.domain

import com.ionspin.kotlin.bignum.decimal.BigDecimal


// FIXME get real rates
internal class CurrencyExchanger {

    private val exchangeRates: Map<Pair<String, String>, BigDecimal> = mutableMapOf(
        Pair("USD", "EUR") to BigDecimal.fromDouble(0.93),
        Pair("EUR", "USD") to BigDecimal.fromDouble(1.07),
        Pair("USD", "RUB") to BigDecimal.fromDouble(83.5),
        Pair("RUB", "USD") to BigDecimal.fromDouble(0.012),
        Pair("EUR", "RUB") to BigDecimal.fromDouble(89.72),
        Pair("RUB", "EUR") to BigDecimal.fromDouble(0.011),
    )

    fun exchange(amount: BigDecimal, fromCurrencyCode: String, toCurrencyCode: String): BigDecimal {
        val rate = exchangeRates[Pair(fromCurrencyCode, toCurrencyCode)]
            ?: throw IllegalArgumentException("No exchange rate for $fromCurrencyCode -> $toCurrencyCode")
        return amount * rate
    }

}