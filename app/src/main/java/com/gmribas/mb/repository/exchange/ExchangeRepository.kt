package com.gmribas.mb.repository.exchange

import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.repository.mapper.ExchangeDetailMapper
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val dataSource: IExchangeDataSource,
    private val mapper: ExchangeDetailMapper
) : IExchangeRepository {
    
    override suspend fun getExchangeDetails(id: Int): ExchangeDetailDTO {
        val response = dataSource.getExchangeInfo(id)
        
        val exchangeInfo = response.data?.values?.firstOrNull()
            ?: throw Exception("Exchange not found")
        
        return mapper.toDTO(exchangeInfo)
    }
}