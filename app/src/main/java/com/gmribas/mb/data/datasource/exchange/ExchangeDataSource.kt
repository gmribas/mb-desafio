package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.ExchangeMapListingResponse
import com.gmribas.mb.data.model.ExchangeResponse
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(
    private val api: CoinMarketCapApi
) : IExchangeDataSource {

    override suspend fun getExchangeInfo(id: Int): ExchangeResponse {
        return api.getExchangeInfo(id)
    }

    override suspend fun getExchangeListings(
        start: Int,
        limit: Int,
        sort: String,
    ): ExchangeMapListingResponse {
        return api.getExchangeListings(start, limit, sort)
    }
}