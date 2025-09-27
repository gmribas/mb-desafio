package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.model.ExchangeDetailResponse

interface IExchangeDataSource {
    suspend fun getExchangeInfo(id: Int): ExchangeDetailResponse
}