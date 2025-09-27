package com.gmribas.mb.repository.mapper

import com.gmribas.mb.data.model.ExchangeInfoData
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import javax.inject.Inject

class ExchangeDetailMapper @Inject constructor() {
    
    fun toDTO(exchangeInfo: ExchangeInfoData): ExchangeDetailDTO {
        return ExchangeDetailDTO(
            id = exchangeInfo.id,
            name = exchangeInfo.name,
            symbol = exchangeInfo.symbol,
            slug = exchangeInfo.slug,
            logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${exchangeInfo.id}.png",
            description = exchangeInfo.description,
            dateAdded = exchangeInfo.dateAdded,
            dateLaunched = exchangeInfo.dateLaunched,
            websiteUrl = exchangeInfo.urls?.website?.firstOrNull(),
            category = exchangeInfo.category,
            platform = exchangeInfo.platform?.name
        )
    }
}