package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.model.ExchangeMapListingResponse

interface IExchangeDataSource {

    suspend fun getExchangeListings(
        start: Int,
        limit: Int,
        sort: String = "volume_24h",
    ): ExchangeMapListingResponse
}