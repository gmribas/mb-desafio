package com.gmribas.mb.domain

sealed interface UseCaseResult<out T : Any> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>
    class Error<out T : Any>(val error: Throwable) : UseCaseResult<T>
}
