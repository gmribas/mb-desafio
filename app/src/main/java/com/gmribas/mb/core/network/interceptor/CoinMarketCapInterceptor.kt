package com.gmribas.mb.core.network.interceptor

import com.gmribas.mb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CoinMarketCapInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("X-CMC_PRO_API_KEY", BuildConfig.COINMARKETCAP_KEY)
            .addHeader("Accept", "application/json")
            .build()
        
        return chain.proceed(modifiedRequest)
    }
}