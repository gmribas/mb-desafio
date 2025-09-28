package com.gmribas.mb.repository.mapper
import com.gmribas.mb.data.model.CryptocurrencyListingResponse
import com.gmribas.mb.repository.dto.CryptocurrencyListingDTO
import com.gmribas.mb.core.IMapper
import javax.inject.Inject

class CryptocurrencyListingMapper @Inject constructor(
    private val cryptocurrencyMapper: CryptocurrencyMapper
) : IMapper<CryptocurrencyListingResponse, CryptocurrencyListingDTO> {
    
    override fun toDTO(model: CryptocurrencyListingResponse): CryptocurrencyListingDTO {
        return CryptocurrencyListingDTO(
            cryptocurrencies = model.data.map { cryptocurrencyMapper.toDTO(it) },
            totalCount = model.status.totalCount
        )
    }

}