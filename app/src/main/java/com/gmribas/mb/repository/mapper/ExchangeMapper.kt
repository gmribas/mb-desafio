package com.gmribas.mb.repository.mapper

import com.gmribas.mb.core.IMapper
import com.gmribas.mb.data.model.ExchangeMapResponse
import com.gmribas.mb.repository.dto.ExchangeDTO
import javax.inject.Inject

class ExchangeMapper @Inject constructor() : IMapper<ExchangeMapResponse, ExchangeDTO> {
    
    override fun toDTO(model: ExchangeMapResponse): ExchangeDTO {
        return ExchangeDTO(
            id = model.id,
            name = model.name,
            slug = model.slug,
            spotVolumeUsd = 0.0,
            logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/${model.id}.png",
            dateLaunched = model.firstHistoricalData
        )
    }

}
