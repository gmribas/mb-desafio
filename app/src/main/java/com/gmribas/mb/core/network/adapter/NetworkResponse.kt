package com.gmribas.mb.core.network.adapter

import okio.IOException

sealed class NetworkResponse<out S : Any> {
    data class Success<S : Any>(val body: S?) : NetworkResponse<S>()
    data class ApiError(val code: Int, val message: String?) : NetworkResponse<Nothing>()
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>()
    data class UnknownError(val error: Throwable) : NetworkResponse<Nothing>()
}
