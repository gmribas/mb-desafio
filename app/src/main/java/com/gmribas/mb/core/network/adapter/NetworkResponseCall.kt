package com.gmribas.mb.core.network.adapter

import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>
) : Call<NetworkResponse<S>> {

    override fun enqueue(callback: Callback<NetworkResponse<S>>) = delegate.enqueue(
        object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()

                if (code in 200..202 || code in 204..299) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.Success(body))
                    )
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(
                            NetworkResponse.ApiError(
                                code = code,
                                message = response.errorBody()?.toString() ?: response.toString()
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                val networkResponse = when (t) {
                    is IOException -> NetworkResponse.NetworkError(t)
                    else -> NetworkResponse.UnknownError(t)
                }
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(networkResponse)
                )
            }

        }
    )

    override fun execute(): Response<NetworkResponse<S>> {
        return try {
            val response = delegate.execute()
            val body = response.body()
            val code = response.code()

            return if (response.isSuccessful) {
                Response.success(NetworkResponse.Success(body))
            } else {
                Response.success(
                    NetworkResponse.ApiError(
                        code,
                        response.errorBody()?.toString() ?: response.toString()
                    )
                )
            }
        } catch (e: IOException) {
            Response.success(NetworkResponse.UnknownError(e))
        }
    }

    override fun clone(): Call<NetworkResponse<S>> = NetworkResponseCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}