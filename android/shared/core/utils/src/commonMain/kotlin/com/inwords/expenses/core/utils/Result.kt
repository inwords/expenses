package com.inwords.expenses.core.utils

sealed interface Result<out T> {

    data class Success<out T : Any>(val data: T) : Result<T>

    data object Error : Result<Nothing>
}

inline fun <T : Any, R : Any> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.data))
        is Result.Error -> this
    }
}

sealed interface IoResult<out T: Any> {

    data class Success<out T : Any>(val data: T) : IoResult<T>

    sealed interface Error : IoResult<Nothing> {
        data object Retry : Error
        data object Failure : Error
    }
}

inline fun <T : Any, R : Any> IoResult<T>.map(transform: (T) -> R): IoResult<R> {
    return when (this) {
        is IoResult.Success -> IoResult.Success(transform(this.data))
        is IoResult.Error.Retry -> this
        is IoResult.Error.Failure -> this
    }
}