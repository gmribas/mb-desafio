package com.gmribas.mb.data.api

import com.gmribas.mb.data.model.AssetsResponse
import com.gmribas.mb.data.model.ExchangeMapListingResponse
import com.gmribas.mb.data.model.ExchangeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapApi {

    @GET("v1/exchange/map")
    suspend fun getExchangeListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("sort") sort: String = "volume_24h",
    ): ExchangeMapListingResponse

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: Int,
    ): ExchangeResponse
    
    @GET("v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: Int
    ): AssetsResponse
}
