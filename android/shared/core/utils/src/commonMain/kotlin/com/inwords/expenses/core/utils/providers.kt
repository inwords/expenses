package com.inwords.expenses.core.utils

fun interface SuspendLazy<T> {

    suspend fun value(): T
}