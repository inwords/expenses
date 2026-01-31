package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.core.utils.divide
import com.ionspin.kotlin.bignum.decimal.BigDecimal


// FIXME get real rates
internal class CurrencyExchanger(
    // How many units of each currency equals 1 USD
    private val usdToOtherRates: Map<String, BigDecimal> = hashMapOf(
        "EUR" to BigDecimal.fromDouble(0.8436),
        "RUB" to BigDecimal.fromDouble(76.3834),
        "TRY" to BigDecimal.fromDouble(43.4272),
        "JPY" to BigDecimal.fromDouble(154.7350),
        "AED" to BigDecimal.fromDouble(3.6725),
        "USD" to BigDecimal.fromDouble(1.0),
    )
) {

    private val directRates: Map<Pair<String, String>, BigDecimal> = buildMap {
        for ((fromCurrency, fromRate) in usdToOtherRates) {
            for ((toCurrency, toRate) in usdToOtherRates) {
                if (fromCurrency != toCurrency) {
                    put(fromCurrency to toCurrency, toRate.divide(other = fromRate, scale = 4))
                }
            }
        }
    }

    fun exchange(amount: BigDecimal, fromCurrencyCode: String, toCurrencyCode: String): BigDecimal {
        if (fromCurrencyCode == toCurrencyCode) {
            return amount
        }

        val rate = directRates[fromCurrencyCode to toCurrencyCode]
            ?: throw IllegalArgumentException("No exchange rate for $fromCurrencyCode -> $toCurrencyCode")
        return amount * rate
    }

}
