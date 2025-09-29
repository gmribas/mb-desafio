package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.model.ExchangeMapListingResponse
import com.gmribas.mb.data.model.ExchangeResponse

interface IExchangeDataSource {

    suspend fun getExchangeListings(
        start: Int,
        limit: Int,
        sort: String = "volume_24h",
    ): ExchangeMapListingResponse

    suspend fun getExchangeInfo(
        id: Int,
    ): ExchangeResponse
}