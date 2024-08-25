package com.inwords.expenses.feature.expenses.domain

import com.ionspin.kotlin.bignum.decimal.BigDecimal


// FIXME get real rates
internal class CurrencyExchanger {

    private val exchangeRates: Map<Pair<String, String>, BigDecimal> = mutableMapOf(
        Pair("USD", "EUR") to BigDecimal.fromDouble(0.89),
        Pair("USD", "RUB") to BigDecimal.fromDouble(91.13),
        Pair("USD", "TRY") to BigDecimal.fromDouble(34.01),
        Pair("USD", "JPY") to BigDecimal.fromDouble(144.30),

        Pair("EUR", "USD") to BigDecimal.fromDouble(1.12),
        Pair("EUR", "RUB") to BigDecimal.fromDouble(101.98),
        Pair("EUR", "TRY") to BigDecimal.fromDouble(38.09),
        Pair("EUR", "JPY") to BigDecimal.fromDouble(161.47),

        Pair("RUB", "USD") to BigDecimal.fromDouble(0.011),
        Pair("RUB", "EUR") to BigDecimal.fromDouble(0.0098),
        Pair("RUB", "TRY") to BigDecimal.fromDouble(0.37),
        Pair("RUB", "JPY") to BigDecimal.fromDouble(1.58),

        Pair("TRY", "USD") to BigDecimal.fromDouble(0.029),
        Pair("TRY", "EUR") to BigDecimal.fromDouble(0.026),
        Pair("TRY", "RUB") to BigDecimal.fromDouble(2.68),
        Pair("TRY", "JPY") to BigDecimal.fromDouble(4.24),

        Pair("JPY", "USD") to BigDecimal.fromDouble(0.0069),
        Pair("JPY", "EUR") to BigDecimal.fromDouble(0.0062),
        Pair("JPY", "RUB") to BigDecimal.fromDouble(0.63),
        Pair("JPY", "TRY") to BigDecimal.fromDouble(0.24)
    )

    fun exchange(amount: BigDecimal, fromCurrencyCode: String, toCurrencyCode: String): BigDecimal {
        val rate = exchangeRates[Pair(fromCurrencyCode, toCurrencyCode)]
            ?: throw IllegalArgumentException("No exchange rate for $fromCurrencyCode -> $toCurrencyCode")
        return amount * rate
    }

}