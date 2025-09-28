package com.gmribas.mb.repository.exchange

import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDetailDTO

interface IExchangeRepository {
    suspend fun getExchangeDetails(id: Int): ExchangeDetailDTO
    suspend fun getExchangeAssets(id: Int): List<ExchangeAssetDTO>
}
