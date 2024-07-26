package com.inwords.expenses.core.ui.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T> MutableStateFlow<SimpleScreenState<T>>.updateIfSuccess(function: (T) -> T) {
    update { state ->
        if (state !is SimpleScreenState.Success) return@update state
        SimpleScreenState.Success(function(state.data))
    }
}