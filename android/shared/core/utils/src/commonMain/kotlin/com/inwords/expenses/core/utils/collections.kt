package com.inwords.expenses.core.utils

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