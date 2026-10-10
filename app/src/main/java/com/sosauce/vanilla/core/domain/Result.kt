package com.sosauce.vanilla.core.domain

interface Error

sealed interface Result<out D, out E : com.sosauce.vanilla.core.domain.Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.sosauce.vanilla.core.domain.Error>(val error: E) : Result<Nothing, E>
}

typealias EmptyResult<E> = Result<Unit, E>
