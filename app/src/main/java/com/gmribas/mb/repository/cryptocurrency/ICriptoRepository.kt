package com.gmribas.mb.repository.cryptocurrency

import com.gmribas.mb.repository.dto.CriptoDetailDTO
import com.gmribas.mb.repository.dto.ExchangeAssetDTO

interface ICriptoRepository {
    suspend fun getCriptoDetails(id: Int): CriptoDetailDTO
    suspend fun getExchangeAssets(id: Int): List<ExchangeAssetDTO>// fixme
}
