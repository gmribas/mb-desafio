package com.gmribas.mb.repository.mapper

import com.gmribas.mb.core.IMapper
import com.gmribas.mb.data.model.ExchangeResponse
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.UrlsDTO
import javax.inject.Inject

class ExchangeMapper @Inject constructor() : IMapper<ExchangeResponse, ExchangeDTO> {
    
    override fun toDTO(model: ExchangeResponse): ExchangeDTO {
        val item = model.data.values.firstOrNull()
        return ExchangeDTO(
            id = item?.id,
            name = item?.name,
            slug = item?.slug,
            spotVolumeUsd = item?.spotVolumeUsd,
            logo = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/${item?.id}.png",
            dateLaunched = item?.dateLaunched,
            makerFee = item?.makerFee,
            takerFee = item?.takerFee,
            description = item?.description,
            urls = item?.urls?.let { urls ->
                UrlsDTO(
                    website = urls.website?.firstOrNull(),
                    twitter = urls.twitter?.firstOrNull(),
                    chat = urls.chat?.firstOrNull(),
                    fee = urls.fee?.firstOrNull()
                )
            }
        )
    }

}
