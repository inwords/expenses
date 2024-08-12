package com.inwords.expenses.core.utils

sealed interface Result<out T> {

    data class Success<out T : Any>(val data: T) : Result<T>

    data object Error : Result<Nothing>
}

inline fun <T : Any, R : Any> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.data))
        is Result.Error -> Result.Error
    }
}