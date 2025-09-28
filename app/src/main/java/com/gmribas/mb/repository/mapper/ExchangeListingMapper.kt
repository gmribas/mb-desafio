package com.gmribas.mb.repository.mapper

import com.gmribas.mb.core.IMapper
import com.gmribas.mb.data.model.ExchangeMapListingResponse
import com.gmribas.mb.repository.dto.ExchangeListingDTO
import javax.inject.Inject

class ExchangeListingMapper @Inject constructor(
    private val exchangeMapper: ExchangeMapper
) : IMapper<ExchangeMapListingResponse, ExchangeListingDTO> {
    
    override fun toDTO(model: ExchangeMapListingResponse): ExchangeListingDTO {
        return ExchangeListingDTO(
            exchanges = model.data.map { exchangeMapper.toDTO(it) },
        )
    }

}