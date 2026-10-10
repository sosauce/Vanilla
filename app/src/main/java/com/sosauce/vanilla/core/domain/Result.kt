package com.sosauce.vanilla.core.domain

interface Error

sealed interface Result<out D, out E : com.sosauce.vanilla.core.domain.Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.sosauce.vanilla.core.domain.Error>(val error: E) : Result<Nothing, E>
}

typealias EmptyResult<E> = Result<Unit, E>

inline fun <T, E : com.sosauce.vanilla.core.domain.Error, R> Result<T, E>.map(
    map: (T) -> R
): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(this.data))
    }
}

inline fun <T, E : com.sosauce.vanilla.core.domain.Error> Result<T, E>.onSuccess(
    action: (T) -> Unit
): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(this.data)
            this
        }
    }
}

inline fun <T, E : com.sosauce.vanilla.core.domain.Error> Result<T, E>.onFailure(
    action: (E) -> Unit
): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

fun <T, E : com.sosauce.vanilla.core.domain.Error> Result<T, E>.asEmptyResult(): EmptyResult<E> {
    return map { }
}
