package com.inwords.expenses.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.collections.immutable.adapters.ImmutableMapAdapter

fun <T> List<T>.asImmutableListAdapter(): ImmutableList<T> {
    return this as? ImmutableList
        ?: ImmutableListAdapter(this)
}

fun <K, V> Map<K, V>.asImmutableMap(): ImmutableMap<K, V> {
    return this as? ImmutableMap
        ?: ImmutableMapAdapter(this)
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("sumOfBigDecimal")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> BigDecimal): BigDecimal {
    var sum: BigDecimal = 0.toBigDecimal()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}