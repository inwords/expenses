package com.inwords.expenses.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.experimental.ExperimentalTypeInference

val Main = Dispatchers.Main
val IO = Dispatchers.IO

fun <T> Flow<T>.collectIn(
    scope: CoroutineScope,
    collector: FlowCollector<T>
) {
    val flow = this
    scope.launch {
        flow.collect(collector)
    }
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTypeInference::class)
inline fun <T, R> Flow<T>.flatMapLatestNoBuffer(
    @BuilderInference crossinline transform: suspend (value: T) -> Flow<R>
): Flow<R> {
    return this.flatMapLatest(transform)
        .buffer(0)
}