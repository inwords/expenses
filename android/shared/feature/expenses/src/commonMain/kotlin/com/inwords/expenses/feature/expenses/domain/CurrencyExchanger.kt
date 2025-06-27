package com.inwords.expenses.feature.expenses.domain

import com.ionspin.kotlin.bignum.decimal.BigDecimal


// FIXME get real rates
internal class CurrencyExchanger {

    private val exchangeRates: Map<Pair<String, String>, BigDecimal> = mapOf(
        Pair("USD", "EUR") to BigDecimal.fromDouble(0.8543),
        Pair("USD", "RUB") to BigDecimal.fromDouble(78.4997),
        Pair("USD", "TRY") to BigDecimal.fromDouble(39.8828),
        Pair("USD", "JPY") to BigDecimal.fromDouble(144.8815),

        Pair("EUR", "USD") to BigDecimal.fromDouble(1.1706),
        Pair("EUR", "RUB") to BigDecimal.fromDouble(91.8953),
        Pair("EUR", "TRY") to BigDecimal.fromDouble(46.7344),
        Pair("EUR", "JPY") to BigDecimal.fromDouble(169.3745),

        Pair("RUB", "USD") to BigDecimal.fromDouble(0.0127),
        Pair("RUB", "EUR") to BigDecimal.fromDouble(0.0109),
        Pair("RUB", "TRY") to BigDecimal.fromDouble(0.5086),
        Pair("RUB", "JPY") to BigDecimal.fromDouble(1.8430),

        Pair("TRY", "USD") to BigDecimal.fromDouble(0.0251),
        Pair("TRY", "EUR") to BigDecimal.fromDouble(0.0214),
        Pair("TRY", "RUB") to BigDecimal.fromDouble(1.9663),
        Pair("TRY", "JPY") to BigDecimal.fromDouble(3.6290),

        Pair("JPY", "USD") to BigDecimal.fromDouble(0.0069),
        Pair("JPY", "EUR") to BigDecimal.fromDouble(0.0059),
        Pair("JPY", "RUB") to BigDecimal.fromDouble(0.5424),
        Pair("JPY", "TRY") to BigDecimal.fromDouble(0.2756)
    )

    fun exchange(amount: BigDecimal, fromCurrencyCode: String, toCurrencyCode: String): BigDecimal {
        val rate = exchangeRates[Pair(fromCurrencyCode, toCurrencyCode)]
            ?: throw IllegalArgumentException("No exchange rate for $fromCurrencyCode -> $toCurrencyCode")
        return amount * rate
    }

}