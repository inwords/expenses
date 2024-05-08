package com.inwords.expenses.core.ui.utils

sealed interface SimpleScreenState<out T> {
    data class Success<T>(val data: T) : SimpleScreenState<T>
    data object Loading : SimpleScreenState<Nothing>
    data object Error : SimpleScreenState<Nothing>
    data object Empty : SimpleScreenState<Nothing>
}