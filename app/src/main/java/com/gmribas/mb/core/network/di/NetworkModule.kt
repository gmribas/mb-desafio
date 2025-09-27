package com.gmribas.mb.core.network.di

import com.gmribas.mb.core.network.interceptor.CoinMarketCapInterceptor
import com.gmribas.mb.data.api.CoinMarketCapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoinMarketCapOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoinMarketCapRetrofit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val COINMARKETCAP_BASE_URL = "https://pro-api.coinmarketcap.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideCoinMarketCapApi(@CoinMarketCapRetrofit retrofit: Retrofit) = retrofit.create(CoinMarketCapApi::class.java)

    @Provides
    @CoinMarketCapRetrofit
    fun provideCoinMarketCapRetrofit(
        @CoinMarketCapOkHttp okHttpClient: OkHttpClient,
        gson: Gson
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(COINMARKETCAP_BASE_URL)
        .build()

    @Provides
    @CoinMarketCapOkHttp
    fun provideCoinMarketCapOkHttpClient(
        coinMarketCapInterceptor: CoinMarketCapInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(loggingInterceptor)
        addInterceptor(coinMarketCapInterceptor)
    }.build()

}
