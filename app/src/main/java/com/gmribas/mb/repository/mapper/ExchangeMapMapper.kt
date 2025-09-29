package com.gmribas.mb.repository.mapper

import com.gmribas.mb.core.IMapper
import com.gmribas.mb.data.model.ExchangeMapResponse
import com.gmribas.mb.repository.dto.ExchangeDTO
import javax.inject.Inject

class ExchangeMapMapper @Inject constructor() : IMapper<ExchangeMapResponse, ExchangeDTO> {
    
    override fun toDTO(model: ExchangeMapResponse): ExchangeDTO {
        return ExchangeDTO(
            id = model.id,
            name = model.name,
            slug = model.slug,
            spotVolumeUsd = model.spotVolumeUsd,
            logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/${model.id}.png",
            dateLaunched = model.firstHistoricalData,
            makerFee = 0.0,
            takerFee = 0.0,
            description = "",
            urls = null
        )
    }
}
