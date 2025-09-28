package com.gmribas.mb.data.datasource.exchangeassets

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.ExchangeAssetsResponse
import javax.inject.Inject

class ExchangeAssetsDataSource @Inject constructor(
    private val api: CoinMarketCapApi
) : IExchangeAssetsDataSource {
    override suspend fun getExchangeAssets(id: Int): ExchangeAssetsResponse {
        return api.getExchangeAssets(id)
    }
}