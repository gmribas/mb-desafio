package com.gmribas.mb.data.api

import com.gmribas.mb.data.model.CryptocurrencyListingResponse
import com.gmribas.mb.data.model.ExchangeAssetsResponse
import com.gmribas.mb.data.model.ExchangeDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapApi {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getLatestListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD",
        @Query("sort") sort: String = "market_cap",
        @Query("sort_dir") sortDir: String = "desc"
    ): CryptocurrencyListingResponse
    
    @GET("v1/cryptocurrency/info")
    suspend fun getCryptocurrencyInfo(
        @Query("id") id: Int
    ): ExchangeDetailResponse
    
    @GET("v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: Int
    ): ExchangeAssetsResponse
}
