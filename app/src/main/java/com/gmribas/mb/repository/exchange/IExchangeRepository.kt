package com.gmribas.mb.repository.exchange

import com.gmribas.mb.repository.dto.ExchangeDetailDTO

interface IExchangeRepository {
    suspend fun getExchangeDetails(id: Int): ExchangeDetailDTO
}