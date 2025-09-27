package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.ExchangeDetailResponse
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(
    private val api: CoinMarketCapApi
) : IExchangeDataSource {
    
    override suspend fun getExchangeInfo(id: Int): ExchangeDetailResponse {
        return api.getCryptocurrencyInfo(id)
    }
}