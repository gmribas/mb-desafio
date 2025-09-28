package com.gmribas.mb.repository.exchange

import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.ExchangeDetailMapper
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val dataSource: IExchangeDataSource,
    private val assetsDataSource: IExchangeAssetsDataSource,
    private val mapper: ExchangeDetailMapper,
    private val assetMapper: ExchangeAssetMapper
) : IExchangeRepository {
    
    override suspend fun getExchangeDetails(id: Int): ExchangeDetailDTO {
        val response = dataSource.getExchangeInfo(id)
        
        val exchangeInfo = response.data?.values?.firstOrNull()
            ?: throw Exception("Exchange not found")
        
        return mapper.toDTO(exchangeInfo)
    }
    
    override suspend fun getExchangeAssets(id: Int): List<ExchangeAssetDTO> {
        val response = assetsDataSource.getExchangeAssets(id)
        println("HUE " + response)
        
        val assets = response.data ?: emptyList()
        
        return assetMapper.toDTOList(assets)
    }
}
