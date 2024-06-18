package com.inwords.expenses.core.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

fun <T> List<T>.asImmutableListAdapter(): ImmutableList<T> {
    return this as? ImmutableList
        ?: ImmutableListAdapter(this)
}