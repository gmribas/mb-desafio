package com.gmribas.mb.repository.cryptocurrency

import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.repository.dto.CriptoDetailDTO
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.CriptoDetailMapper
import javax.inject.Inject

class CriptoRepository @Inject constructor(
    private val dataSource: ICryptocurrencyDataSource,
    private val assetsDataSource: IExchangeAssetsDataSource,
    private val mapper: CriptoDetailMapper,
    private val assetMapper: ExchangeAssetMapper
) : ICriptoRepository {
    
    override suspend fun getCriptoDetails(id: Int): CriptoDetailDTO {
        val response = dataSource.getExchangeInfo(id)
        
        val exchangeInfo = response.data?.values?.firstOrNull()
            ?: throw Exception("Exchange not found")
        
        return mapper.toDTO(exchangeInfo)
    }
    
    override suspend fun getExchangeAssets(id: Int): List<ExchangeAssetDTO> {
        val response = assetsDataSource.getExchangeAssets(id)
        val assets = response.data ?: emptyList()
        return assetMapper.toDTOList(assets)
    }
}
