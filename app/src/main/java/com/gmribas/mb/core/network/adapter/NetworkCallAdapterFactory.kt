package com.gmribas.mb.core.network.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // when using in suspend functions the return type will be wrapped in a Call type
        if (getRawType(returnType) != Call::class.java) return null

        check(returnType is ParameterizedType) {
            "returnType must be a Call<NetworkResponse<T>>"
        }

        val response = getParameterUpperBound(0, returnType)

        check(response is ParameterizedType) {
            "response must be a Call<NetworkResponse<T>>"
        }

        val body = getParameterUpperBound(0, returnType)

        return NetworkResponseAdapter<Any>(body)
    }
}
