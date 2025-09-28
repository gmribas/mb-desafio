package com.gmribas.mb.data.datasource.exchangeassets

import com.gmribas.mb.data.model.ExchangeAssetsResponse

interface IExchangeAssetsDataSource {
    suspend fun getExchangeAssets(id: Int): ExchangeAssetsResponse
}