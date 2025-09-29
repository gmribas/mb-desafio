package com.gmribas.mb.data.datasource.exchangeassets

import com.gmribas.mb.data.model.AssetsResponse

interface IExchangeAssetsDataSource {
    suspend fun getExchangeAssets(id: Int): AssetsResponse
}